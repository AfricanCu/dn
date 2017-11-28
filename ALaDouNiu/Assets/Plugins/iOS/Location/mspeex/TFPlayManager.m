//
//  TFPlayMenager.m
//  TengFei
//
//  Created by Jenuce on 2016/11/15.
//  Copyright © 2016年 TengFei. All rights reserved.
//

#import "TFPlayManager.h"
#import "AQPlayer.h"
#import <AVFoundation/AVFoundation.h>
#import "SpeexCodec.h"
#define DESIRED_BUFFER_SIZE 4096

@interface TFPlayManager(){
    ogg_stream_state ogg_stream_state_;
    ogg_sync_state ogg_sync_state_;
    int ogg_packet_no;
    
    BOOL b_first_;
}
@property (nonatomic,retain) AQPlayer *player_;
@property (nonatomic,retain) NSMutableArray* player_files_;
@property (strong,nonatomic) dispatch_queue_t queue_;
@property (nonatomic,retain) NSString* current_file_;
@end


@implementation TFPlayManager
@synthesize delegate;
- (id)init{
    self = [super init];
    if (self) {
        static uint32_t index = 0;
        char queue_n[64]={0};
        sprintf(queue_n, "TFPlayManager_%d",index++);
        self.queue_ = dispatch_queue_create(queue_n, DISPATCH_QUEUE_PRIORITY_DEFAULT);
        index = index%10000000;
        b_first_ = YES;
        self.player_files_ = [NSMutableArray array];
    }
    return self;
}
- (Boolean)isPlaying{
    return !self.player_.bstop;
}
-(void)addPlayFiles:(NSArray*)array{
    dispatch_async(_queue_, ^{
        for (NSString* f in array) {
            if (![self.player_files_ containsObject:f]) {
                [self.player_files_ addObjectsFromArray:array];
                [self start];
            }
        }
    });
}
-(void)addPlayFile:(NSString*)file{
    dispatch_async(_queue_, ^{
        if (![self.player_files_ containsObject:file]){
            [self.player_files_ addObject:file];
            [self start];
        }
    });
}
-(void)addToFirst:(NSString*)file{
    dispatch_async(_queue_, ^{
        if (![self.player_files_ containsObject:file]){
            [self.player_files_ insertObject:file atIndex:0];
            [self.player_ pass];
            [self start];
        }
    });
}


-(void) start{
    //NSLog(@"play start");
    if (!self.player_) {
        self.player_ = [[AQPlayer alloc] init];
        self.player_.delegate_ = (id)self;
    }
    [self.player_ start];
}
-(void) pass{
    dispatch_async(_queue_, ^{
        [self.player_ pass];
    });
}
-(void) stop{
    NSLog(@"play stop");
    dispatch_async(_queue_, ^{
        [self.player_files_ removeAllObjects];
        [self.player_ stop];
    });
}


///////////////////////////////////////////////////////
- (void )convertOggToPCMWithData:(NSData *)oggData {
    int put_count = 0;
    const Byte *oggBytes = [oggData bytes];
    int oggByteSize = (int)[oggData length];
    int readedBytes = 0;
    NSUInteger decodedByteLength = 0;
    
    ogg_packet_no = 0;
    int pageNo = 0;
    
    ogg_sync_init(&ogg_sync_state_);
    ogg_stream_init(&ogg_stream_state_, 0);
    
    bool isPlaying = true;
    while (isPlaying) {
        
        int byteSizeToRead = oggByteSize - readedBytes;
        if (byteSizeToRead > DESIRED_BUFFER_SIZE) {
            byteSizeToRead = DESIRED_BUFFER_SIZE;
        }
        char *buffer = ogg_sync_buffer(&ogg_sync_state_, DESIRED_BUFFER_SIZE);
        memcpy(buffer, oggBytes, byteSizeToRead);    //!!!
        oggBytes += byteSizeToRead;
        readedBytes += byteSizeToRead;
        NSLog(@"byteSizeToRead = %d, oggByteSize = %d, readedBytes = %d", byteSizeToRead, oggByteSize, readedBytes);
        
        int resultSyncWrote = ogg_sync_wrote(&ogg_sync_state_, byteSizeToRead);
        if (resultSyncWrote == -1) {
            //NSLog(@"error:the number of bytes written overflows the internal storage of the ogg_sync_state struct or an internal error occurred.");
            return;
        }
        
        while (YES) {
            ogg_page oggPage;
            int resultSyncPageout= ogg_sync_pageout(&ogg_sync_state_, &oggPage);
            if (resultSyncPageout == 1) {
                //NSLog(@"to decode a page which was synced and returned");
                
                //检查header和comment
                if(ogg_packet_no == 0) {
                    //NSLog(@"it's the header page, check the header later");
                    if ([self readOggHeaderToStreamState:&ogg_stream_state_ fromOggPage:&oggPage]) {
                        ogg_stream_state_.packetno = ogg_packet_no ++;
                        pageNo ++;
                    }
                    else {
                        ogg_packet_no = 0;
                    }
                    continue;
                }
                else if(ogg_packet_no == 1) {
                    //NSLog(@"it's the comment");
                    ogg_stream_state_.packetno = ogg_packet_no ++;
                    pageNo ++;
                    continue;
                }
                else {
                    ogg_stream_state_.pageno = pageNo ++;
                }
                
                int resultStreamPagein = ogg_stream_pagein(&ogg_stream_state_, &oggPage);
                if (resultStreamPagein == -1) {
                    //NSLog(@"ogg_stream_pagein failure");
                    return;
                }
                
                short decodedBuffer[1024];
                SpeexCodec *codec = [[SpeexCodec alloc] init];
                [codec open:4];
                while (YES) {

                    ogg_packet oggPacket;
                    int packetResult = ogg_stream_packetout(&ogg_stream_state_, &oggPacket);
                    if (packetResult == 1) {
                        put_count++;
                        ogg_packet_no ++;
                        int nDecodedByte = sizeof(short) * [codec decode:oggPacket.packet length:(int)oggPacket.bytes output:decodedBuffer];
                        decodedByteLength += nDecodedByte;
                        [self packetDecoded:(Byte *)decodedBuffer size:nDecodedByte];
                    }
                    else if (packetResult == 0) {
                        //need more
                        //NSLog(@"ogg_stream_packetout  error..");
                        break;
                    }
                    else {
                        break;
                    }
                }
                
                [codec close];
                codec = nil;
            }
            else if (resultSyncPageout == 0) {
                //NSLog(@"not enough to decode a page or error");
                break;
            }
            else {
                //NSLog(@"stream has not yet captured sync");
            }
        }
        
        if (byteSizeToRead < DESIRED_BUFFER_SIZE) {
            break;
        }
    }
    if (put_count==0) {
        //NSLog(@"play over");
        [self playover];
    }
}
- (BOOL)readOggHeaderToStreamState:(ogg_stream_state *)os fromOggPage:(ogg_page *)op {
    if (op->body_len != 80) {
        return NO;
    }
    os->serialno = ogg_page_serialno(op);
    return YES;
}
//packet转换完成
- (void)packetDecoded:(Byte *)decodedData size:(int)dataSize {
    [_player_ putMediaData:[NSData dataWithBytes:decodedData length:dataSize]];
}

/////AQPlayter delegate

-(void)playover{
    dispatch_async(_queue_, ^{
        if (self.player_.bstop) {
            //NSLog(@"play stop 0.................%@",self.current_file_);
            b_first_ = YES;
            if ([self.delegate respondsToSelector:@selector(onPlayover:)]) {
                [delegate onPlayover:self.current_file_];
            }
            if ([self.delegate respondsToSelector:@selector(onWillstop)]) {
                [delegate onWillstop];
            }
            return;
        }
        if ([_player_files_ count]==0) {
                [self.player_ stop];
                //if ([self.delegate respondsToSelector:@selector(onWillstop)]) {
                //    [delegate onWillstop];
                //}
            //}
            //[self.player_ stop];
            return ;
        }
        if (!b_first_) {
            if ([self.delegate respondsToSelector:@selector(onPlayover:)]) {
                [delegate onPlayover:self.current_file_];
            }
        }
        b_first_ = NO;
        self.current_file_ = [_player_files_ firstObject];
        if ([self.delegate respondsToSelector:@selector(onWillplay:)]) {
            [delegate onWillplay:self.current_file_];
        }
        //NSLog(@"play start .................%@",self.current_file_);
        [self convertOggToPCMWithData:[NSData dataWithContentsOfFile:self.current_file_]];
        [_player_files_ removeObjectAtIndex:0];
    });
}
-(void)playerError:(NSString*)error{
    NSLog(@"%@", error);
}

@end
