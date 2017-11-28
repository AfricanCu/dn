//
//  TFEncapsulator.m
//  TengFei
//
//  Created by Jenuce on 2016/11/14.
//  Copyright © 2016年 TengFei. All rights reserved.
//

#import "TFEncapsulator.h"
#import "SpeexCodec.h"
#import "TFRecordManager.h"
#define OGG_MODE 0
#define OGG_VBR  1
#define FRAME_SIZE 160
#define RATE  8000
#define CHANNEL_COUNT 1

@interface TFEncapsulator(){
    SpeexHeader speex_header_;
    
    ogg_int64_t packet_count_;
    ogg_int16_t granulepos_;
    ogg_stream_state ogg_stream_state_;
    ogg_page ogg_page_;
    
    double pcm_total_;
}
@property (strong,nonatomic) NSMutableData *ring_buffer_;
@property (strong,nonatomic) dispatch_queue_t queue_;
@property (strong,nonatomic) SpeexCodec *codec_;
@property (strong,nonatomic) NSMutableData *ogg_buffer_;

@property (strong,nonatomic) NSString* out_file_name_;
@property (strong,nonatomic) NSFileHandle *out_file_;
@end

@implementation TFEncapsulator
- (id)init{
    self = [super init];
    if (self) {
        static uint32_t index = 0;
        char queue_n[64]={0};
        sprintf(queue_n, "TFEncapsulator_%d",index++);
        self.queue_ = dispatch_queue_create(queue_n, DISPATCH_QUEUE_PRIORITY_DEFAULT);
        index = index%10000000;
        
        _ring_buffer_ = [NSMutableData data];
        
        speex_init_header(&speex_header_, RATE, CHANNEL_COUNT, &speex_nb_mode);
        
        
    }
    return self;
}
- (void)dealloc{
    [self closeOutFile];
}

- (void)inputPCMDataFromBuffer:(Byte *)buffer size:(UInt32)dataSize {
    pcm_total_ +=dataSize;
    __block NSData *temp_data = [NSData dataWithBytes:buffer length:dataSize];
    dispatch_async(self.queue_, ^{
        [self.ring_buffer_ appendData:temp_data];
        while ([self.ring_buffer_ length] >= FRAME_SIZE * 2) {
            NSData *pcmData = [NSData dataWithBytes:[self.ring_buffer_ bytes] length:FRAME_SIZE * 2];
            
            NSData *speexData = [self.codec_ encode:(short *)[pcmData bytes] length:(int)[pcmData length]/sizeof(short)];
            [self inputOggPacketFromSpeexData:speexData];
            
            Byte *dataPtr = (Byte *)[self.ring_buffer_ bytes];
            dataPtr += FRAME_SIZE * 2;
            self.ring_buffer_ = [NSMutableData dataWithBytesNoCopy:dataPtr length:[self.ring_buffer_ length] - FRAME_SIZE * 2 freeWhenDone:NO];
        }
    });
}
- (void)prepareForEncapsulating {
    
    dispatch_async(self.queue_, ^{
        ogg_stream_init(&ogg_stream_state_, arc4random()%8888);
        self.ogg_buffer_ = [NSMutableData data];
        
        self.codec_ = [[SpeexCodec alloc] init];
        [self.codec_ open:4];
        [self writeHeaderWithComment:@"tf"];
        pcm_total_ = 0;
        
    });
    
}
- (void)stopEncapsulating:(BOOL)bCancel {
    dispatch_async(_queue_, ^{
        if (self.codec_==nil) {
            return ;
        }
        [self outputAPage:YES endOfSteam:NO];
        [self closeOutFile];
        [self.codec_ close];
        self.codec_ = nil;
        self.ogg_buffer_ = [NSMutableData data];
        ogg_stream_clear(&ogg_stream_state_);
        NSLog(@"sound len=%f",pcm_total_/16/1000);
        if ([self.delegete respondsToSelector:@selector(encapsulatingOver:soundTimeLong:cancel:)]) {
            [self.delegete encapsulatingOver:self.out_file_name_ soundTimeLong:pcm_total_/16/1000 cancel:bCancel];
        }
    });
}

- (void)setFileName:(NSString *)filename{
    __block NSString* temp_file_name = filename;
    dispatch_async(self.queue_, ^{
        NSFileManager *fileManager = [NSFileManager defaultManager];
        if ( ! [fileManager fileExistsAtPath:filename]) {
            [fileManager createFileAtPath:filename contents:nil attributes:nil];
        }
        [self closeOutFile];
        self.out_file_name_ = temp_file_name;
        self.out_file_ = [NSFileHandle fileHandleForUpdatingAtPath:_out_file_name_];
    });
}
///////////////////////////////////////////////////////////////////////////
static void writeInt(unsigned char *dest, unsigned long offset, unsigned long value) {
    for(int i = 0;i < 4;i++) {
        dest[offset + i]=(unsigned char)(0xff & ((unsigned long)value)>>(i*8));
    }
}

static void writeString(unsigned char *dest, unsigned long offset, unsigned char *value, unsigned long length) {
    unsigned char *tempPointr = dest + offset;
    memcpy(tempPointr, value, length);
}
///写ogg头
- (void)writeHeaderWithComment:(NSString *)comment {
    
    packet_count_ = 0;
    granulepos_ = 0;
    
    //first, write the ogg header page
    unsigned char speexHeader[80];
    
    unsigned long offset = 0;
    writeString(speexHeader, offset+0, (unsigned char *)"Speex   ", 8);    //  0 -  7: speex_string
    int versionSize = sizeof(speex_header_.speex_version);
    
    writeString(speexHeader, offset+8, (unsigned char *)speex_header_.speex_version, versionSize);  //8 - 27: speex_version
    writeInt(speexHeader, offset+28, 1);           // 28 - 31: speex_version_id
    writeInt(speexHeader, offset+32, 80);          // 32 - 35: header_size
    writeInt(speexHeader, offset+36, 8000);  // 36 - 39: rate
    writeInt(speexHeader, offset+40, 0);        // 40 - 43: mode (0=NB, 1=WB, 2=UWB)
    writeInt(speexHeader, offset+44, 4);           // 44 - 47: mode_bitstream_version
    writeInt(speexHeader, offset+48, 1);    // 48 - 51: nb_channels
    writeInt(speexHeader, offset+52, -1);          // 52 - 55: bitrate
    writeInt(speexHeader, offset+56, 160 << 0); // 56 - 59: frame_size (NB=160, WB=320, UWB=640)
    writeInt(speexHeader, offset+60, 1);     // 60 - 63: vbr
    writeInt(speexHeader, offset+64, 1);     // 64 - 67: frames_per_packet
    writeInt(speexHeader, offset+68, 0);           // 68 - 71: extra_headers
    writeInt(speexHeader, offset+72, 0);           // 72 - 75: reserved1
    writeInt(speexHeader, offset+76, 0);           // 76 - 79: reserved2
    
    ogg_packet speexHeaderPacket;
    speexHeaderPacket.packet = (unsigned char *)speexHeader;
    speexHeaderPacket.bytes = 80;
    speexHeaderPacket.b_o_s = 1;
    speexHeaderPacket.e_o_s = 0;
    speexHeaderPacket.granulepos = 0;
    speexHeaderPacket.packetno = packet_count_++;
    
    ogg_stream_packetin(&ogg_stream_state_, &speexHeaderPacket);
    [self outputAPage:YES endOfSteam:NO];
    
    
    offset = 0;
    const char *commentChars = [comment cStringUsingEncoding:NSUTF8StringEncoding];
    unsigned long length = [comment lengthOfBytesUsingEncoding:NSUTF8StringEncoding];
    unsigned char speexCommentHeader[length + 8];
    writeInt(speexCommentHeader, offset, length);       // vendor comment size
    writeString(speexCommentHeader, offset+4, (unsigned char *)commentChars, length); // vendor comment
    writeInt(speexCommentHeader, offset+length+4, 0);   // user comment list length
    
    ogg_packet speexCommentPacket;
    speexCommentPacket.packet = (unsigned char *)speexCommentHeader;
    speexCommentPacket.bytes = length + 8;
    speexCommentPacket.b_o_s = 0;
    speexCommentPacket.e_o_s = 0;
    speexCommentPacket.granulepos = 0;
    speexCommentPacket.packetno = packet_count_++;
    
    ogg_stream_packetin(&ogg_stream_state_, &speexCommentPacket);
    [self outputAPage:YES endOfSteam:NO];
}
////音频流写入
- (void)inputOggPacketFromSpeexData:(NSData *)data {
    ogg_packet packet;
    packet.packet = (unsigned char *)[data bytes];
    packet.bytes = (long)([data length]);
    packet.b_o_s = 0;
    packet.e_o_s = 0;
    granulepos_ += FRAME_SIZE;
    packet.granulepos = granulepos_;
    packet.packetno = packet_count_++;
    ogg_stream_packetin(&ogg_stream_state_, &packet);
    
    [self checkPageSufficient];
}
//检查packet是否足够生成一个page
- (void)checkPageSufficient {
    [self outputAPage:NO endOfSteam:NO];
}
//将页保存至文件并重置一些计数器。是否关闭文件。
- (void)outputAPage:(BOOL)isHeaderOrComment endOfSteam:(BOOL)endOfStream {
    if (isHeaderOrComment || endOfStream) {
        ogg_stream_flush(&ogg_stream_state_, &ogg_page_);
        [_ogg_buffer_ appendBytes:ogg_page_.header length:ogg_page_.header_len];
        [_ogg_buffer_ appendBytes:ogg_page_.body length:ogg_page_.body_len];
        [self writeDataToFile:_ogg_buffer_];
        [_ogg_buffer_ setLength:0];
    }
    else {
        if (ogg_stream_pageout(&ogg_stream_state_, &ogg_page_)) {
            [_ogg_buffer_ appendBytes:ogg_page_.header length:ogg_page_.header_len];
            [_ogg_buffer_ appendBytes:ogg_page_.body length:ogg_page_.body_len];
            [self writeDataToFile:_ogg_buffer_];
            [_ogg_buffer_ setLength:0];
        }
    }
    
}

- (void)writeDataToFile:(NSData *)newData {
    [_out_file_ seekToEndOfFile];
    [_out_file_ writeData:newData];
}
- (void)closeOutFile{
    if (_out_file_) {
        [_out_file_ closeFile];
        _out_file_ = nil;
    }
}
@end
