//
//  TFAudioManager.m
//  OggSpeex
//
//  Created by Jenuce on 2016/11/17.
//  Copyright © 2016年 Sense Force. All rights reserved.
//

#import "TFAudioManager.h"
#import <AVFoundation/AVFoundation.h>
#import <AudioToolbox/AudioToolbox.h>

#import "TFRecordManager.h"
#import "TFPlayManager.h"
#import "AFNetworking.h"

@interface TFSendTask : NSObject
@property (strong,nonatomic) NSString *filePath;
@property (strong,nonatomic) NSDate* date;
@property (assign,nonatomic) NSTimeInterval interval;
@end
@implementation TFSendTask
@end

@interface TFAudioManager (){
    int delegate_;
}
@property (strong,nonatomic) dispatch_queue_t queue_;

@property (nonatomic,retain) TFRecordManager* recorder_;
@property (nonatomic,retain) TFPlayManager* player_;
@property (nonatomic,retain) AFHTTPSessionManager *http_manager_up_;
@property (nonatomic,retain) AFURLSessionManager *http_manager_down_;
@property (nonatomic,retain) NSDictionary* config_;

@property (nonatomic,retain) NSMutableArray* voice_list_;  //map{key:local_file value:remote_url}
@property (nonatomic,assign) BOOL bStopRecord_;

@property (nonatomic,retain) NSMutableArray* send_file_list_;   //TFSendTask
@property (nonatomic,retain) NSMutableArray* down_file_list_;   //string url

@property (nonatomic,retain) NSMutableArray* white_voice_list_;   //存储每个人的最后一条语音

@property (nonatomic,assign) UInt32 inInterruptionState_;
@end
@implementation TFAudioManager
- (id)init{
    self = [super init];
    if (self) {
        self.recorder_ = [[TFRecordManager alloc] init];
        self.recorder_.delegate = (id)self;
        self.player_ = [[TFPlayManager alloc] init];
        self.player_.delegate = (id)self;
        self.voice_list_ = [NSMutableArray array];
        self.queue_ = dispatch_queue_create("TFAudioManager", DISPATCH_QUEUE_PRIORITY_DEFAULT);
        _bStopRecord_ = YES;
        delegate_ = -1;
        //[self startProximityMonitering];
        AudioSessionInitialize(NULL, NULL, interruptionListener, (__bridge void *)self);
        
        self.send_file_list_= [NSMutableArray array];
        self.down_file_list_ = [NSMutableArray array];
        self.white_voice_list_ = [NSMutableArray array];
        self.inInterruptionState_ = kAudioSessionEndInterruption;
    }
    return self;
}
-(void)dealloc{
    [self stopProximityMonitering];
}
/*-(void)setDelegate:(NSDictionary*)delegate{
    if (delegate_>0) {
        cocos2d::LuaBridge::releaseLuaFunctionById(delegate_);
        delegate_=-1;
    }
    delegate_ = [[delegate objectForKey:@"delegate"] intValue];
}*/

//json格式字符串转字典：

- (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString {
    
    if (jsonString == nil) {
        
        return nil;
        
    }
    
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    NSError *err;
    
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                         
                                                        options:NSJSONReadingMutableContainers
                         
                                                          error:&err];
    
    if(err) {
        
        NSLog(@"json解析失败：%@",err);
        
        return nil;
        
    }
    
    return dic;
    
}

- (NSString*)dictionaryToJson:(NSDictionary *)dic

{
    
    NSError *parseError = nil;
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&parseError];
    
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
}

//////////////////////record
-(void)startRecord:(NSDictionary*)config{
    dispatch_async(_queue_, ^{
        //NSLog(@"TFAudioMenager startRecord");
        [self initAudioSession];
        [self.player_ stop];
        self.config_ = config;
        [self.recorder_ start:[TFAudioManager defaultFileName]];
    });
}
-(void)stopRecord{
    dispatch_async(_queue_, ^{
        //NSLog(@"TFAudioMenager stopRecord");
        [self.recorder_ stop];
    });
}
-(void)cancelRecord{
    dispatch_async(_queue_, ^{
        //NSLog(@"TFAudioMenager cancelRecord");
        [self.recorder_ cancel];
    });
}


//////////////////////play
-(void)addPlay:(NSDictionary*)file{
    @synchronized (self.down_file_list_) {
        [self.down_file_list_ addObject:[file objectForKey:@"url"]];
        if ([self.down_file_list_ count]==1) {
            NSLog(@"%@",file);
            [self doDownLoadFile:[file objectForKey:@"url"]];
        }
    }
}
-(void)passPlay{
    dispatch_async(_queue_, ^{
        if ([self.player_ isPlaying]){
            [self.player_ pass];
        }else{
            @synchronized (self.voice_list_){
                NSMutableArray *array = [NSMutableArray array];
                for (NSDictionary*dic in self.voice_list_) {
                    [array addObject:[[dic allKeys] objectAtIndex:0]];
                }
                [self.player_ addPlayFiles:array];
            }
            
        }
    });
}
-(void)stopPlay:(Boolean)force{
    if (force) {
        [self cleanPlayResouce];
    }
    dispatch_async(_queue_, ^{
        [self.player_ stop];
    });
}
-(void)cleanPlaysCache{
    @synchronized (self.white_voice_list_) {
        NSFileManager *fm=[NSFileManager defaultManager];
        for (NSDictionary* dic in self.white_voice_list_) {
            [fm removeItemAtPath:[[dic allKeys] objectAtIndex:0] error:nil];
        }
        [self.white_voice_list_ removeAllObjects];
    }
}
-(void)playOneLast:(NSDictionary*)one{
    NSString * uid = [one objectForKey:@"userid"];
    @synchronized (self.white_voice_list_) {
        for (NSDictionary* dic in self.white_voice_list_) {
            NSString * net_url = [[dic allValues] firstObject];
            NSURL* url = [NSURL URLWithString:net_url];
            NSDictionary* params = [self dictionaryFromQuery:[url query]];
            if ([[params objectForKey:@"recorder_id"] isEqualToString:uid]) {
//                @synchronized (self.voice_list_) {
//                    Boolean bHave = NO;
//                    for (NSDictionary* m in self.voice_list_) {
//                        if ([[m allKeys] containsObject:[[dic allKeys] firstObject]]) {
//                            bHave = YES;
//                            break;
//                        }
//                    }
//                    if (!bHave){
//                        [_voice_list_ addObject:dic];
//                    }
//                    
//                }
                if (self.bStopRecord_) {
                    [self.player_ addToFirst:[[dic allKeys] firstObject]];
                }
                break;
            }

        }
    }
}
/////////////////////////////////////////////////////////////////////////
- (void)cleanPlayResouce{
    @synchronized (self.down_file_list_) {
        [self.down_file_list_ removeAllObjects];
    }
    @synchronized (self.voice_list_) {
        NSFileManager *fm=[NSFileManager defaultManager];
        for (NSDictionary* dic in _voice_list_) {
            [fm removeItemAtPath:[[dic allKeys] objectAtIndex:0] error:nil];
        }
        [_voice_list_ removeAllObjects];
    }
}

-(void)doDownLoadFile:(NSString*)file{
    if (!self.http_manager_down_) {
        NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
        self.http_manager_down_ = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
    }
    NSURL *URL = [NSURL URLWithString:file];
    if ([file rangeOfString:@"?"].location == NSNotFound) {
        file = [file substringToIndex:[file length]-[[URL parameterString] length]];
        file = [NSString stringWithFormat:@"%@userid=%@&token=%@&appid=%@&roomid=%@",
                file,[self.config_ valueForKey:@"userid"],[self.config_ valueForKey:@"token"],
                [self.config_ valueForKey:@"appid"],[self.config_ valueForKey:@"roomid"]];
    }
    
    NSURLRequest *request = [NSURLRequest requestWithURL:URL];
    //NSLog(@"download url = %@",URL);
    NSURLSessionDownloadTask *downloadTask = [self.http_manager_down_ downloadTaskWithRequest:request progress:nil destination:^NSURL *(NSURL *targetPath, NSURLResponse *response) {
        NSURL *documentsDirectoryURL = [NSURL URLWithString:[TFAudioManager defaultFilePath2]];
        return [documentsDirectoryURL URLByAppendingPathComponent:[response suggestedFilename]];
    } completionHandler:^(NSURLResponse *response, NSURL *filePath, NSError *error) {
        if (filePath) {
            @synchronized (self.voice_list_) {
                [_voice_list_ addObject:@{[filePath relativePath]:file}];
                if (self.bStopRecord_ && kAudioSessionBeginInterruption!=self.inInterruptionState_) {
                    [self.player_ addPlayFile:[filePath relativePath]];
                }
            }
        }
        @synchronized (self.down_file_list_) {
            if ([self.down_file_list_ count]>0){
                [self.down_file_list_ removeObjectAtIndex:0];
            }
            if ([self.down_file_list_ count]>0) {
                [self doDownLoadFile:[self.down_file_list_ firstObject]];
            }
        }
    }];
    
    [downloadTask resume];
}
- (NSDictionary*)dictionaryFromQuery:(NSString*)query {
    NSCharacterSet* delimiterSet = [NSCharacterSet characterSetWithCharactersInString:@"&;"];
    NSMutableDictionary* pairs = [NSMutableDictionary dictionary];
    NSScanner* scanner = [[NSScanner alloc] initWithString:query];
    while (![scanner isAtEnd]) {
        NSString* pairString = nil;
        [scanner scanUpToCharactersFromSet:delimiterSet intoString:&pairString];
        [scanner scanCharactersFromSet:delimiterSet intoString:NULL];
        NSArray* kvPair = [pairString componentsSeparatedByString:@"="];
        if (kvPair.count == 2) {
            NSString* key = [[kvPair objectAtIndex:0]
                             stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            NSString* value = [[kvPair objectAtIndex:1]
                               stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            [pairs setObject:value forKey:key];
        }
    }
    
    return [NSDictionary dictionaryWithDictionary:pairs];
}
//////////////////////////////////////////////////
+ (NSString*) defaultFilePath{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *voiceDirectory = [documentsDirectory stringByAppendingPathComponent:@"voice"];
    if ( ! [[NSFileManager defaultManager] fileExistsAtPath:voiceDirectory]) {
        [[NSFileManager defaultManager] createDirectoryAtPath:voiceDirectory withIntermediateDirectories:YES attributes:nil error:NULL];
    }
    return voiceDirectory;
}
+ (NSString*) defaultFilePath2{
    [TFAudioManager defaultFilePath];
    NSURL *documentsDirectoryURL = [[NSFileManager defaultManager] URLForDirectory:NSDocumentDirectory inDomain:NSUserDomainMask appropriateForURL:nil create:NO error:nil];
    documentsDirectoryURL = [documentsDirectoryURL URLByAppendingPathComponent:@"voice"];
    return [documentsDirectoryURL absoluteString];
}
+ (NSString *)defaultFileName {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *voiceDirectory = [documentsDirectory stringByAppendingPathComponent:@"voice"];
    if ( ! [[NSFileManager defaultManager] fileExistsAtPath:voiceDirectory]) {
        [[NSFileManager defaultManager] createDirectoryAtPath:voiceDirectory withIntermediateDirectories:YES attributes:nil error:NULL];
    }
    return [voiceDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"up_%@.spx",[NSNumber numberWithInteger:[[NSDate date] timeIntervalSince1970]]]];
    //return [voiceDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"up_%d.spx",1]];
}

///////////////recode delegate///////////
- (void)onRecordStart{
    @synchronized (self.voice_list_) {
        _bStopRecord_ = NO;
    }
    dispatch_async(dispatch_get_main_queue(), ^{
		NSMutableDictionary *dic = @{}.mutableCopy;
		[dic setObject:@"OnRecordStart" forKey:@"Event"];
		NSString *rtn = [self dictionaryToJson:dic];
        UnitySendMessage("TFVoiceTool", "PlayFuncMsg", rtn.UTF8String);
    });
}
- (void)onRecordStop:(NSString *)filePath startDate:(NSDate*)date soundTime:(NSTimeInterval)interval cancel: (BOOL)bCancel{
    if (interval<=0.55) {
        bCancel = YES;
    }
    if (bCancel) {
        @synchronized (self.voice_list_) {
            _bStopRecord_ = YES;
            NSMutableArray *array = [NSMutableArray array];
            for (NSDictionary*dic in self.voice_list_) {
                [array addObject:[[dic allKeys] objectAtIndex:0]];
            }
            [self.player_ addPlayFiles:array];
            
        }
        NSFileManager *fm=[NSFileManager defaultManager];
        [fm removeItemAtPath:filePath error:nil];
        [self doDelegateOnRecordStop:nil soundTime:interval cancel:YES];
    }else{
        @synchronized (self.voice_list_) {
            _bStopRecord_ = YES;
            [self.voice_list_ insertObject:@{filePath:[NSString stringWithFormat:@"%@?recorder_id=%@&appid=%@&roomid=%@&record_time=%f&record_len=%.3f",
                                                    [self.config_ valueForKey:@"url"],
                                                    [self.config_ valueForKey:@"userid"],
                                                    [self.config_ valueForKey:@"appid"],
                                                    [self.config_ valueForKey:@"roomid"],
                                                    [date timeIntervalSince1970],
                                                       interval]} atIndex:0];
            //[self.player_ addToFirst:filePath];
            NSMutableArray *array = [NSMutableArray array];
            for (NSDictionary*dic in self.voice_list_) {
                [array addObject:[[dic allKeys] objectAtIndex:0]];
            }
            [self.player_ addPlayFiles:array];
        }
        @synchronized (self.send_file_list_) {
            TFSendTask* task = [[TFSendTask alloc] init];
            task.filePath = filePath;
            task.interval = interval;
            task.date = date;
            [self.send_file_list_ addObject:task];
            if ([self.send_file_list_ count]==1) {
                [self sendFile:task];
            }
        }
    }
    
}
- (void)doDelegateOnRecordStop:(NSString*)param soundTime:(NSTimeInterval)interval cancel:(BOOL)bcanel{
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString* bc = [NSString stringWithFormat:@"%d",bcanel];
		NSNumber* sl = [NSNumber numberWithFloat:interval];
/*#ifdef COCOA2D_LUA
        cocos2d::LuaBridge::pushLuaFunctionById(delegate_);
        cocos2d::LuaValueDict item;
        cocos2d::LuaValueDict p;
        item["Event"] = cocos2d::LuaValue::stringValue("OnRecordStop");
        p["BCancel"] = cocos2d::LuaValue::stringValue([bc cStringUsingEncoding:NSUTF8StringEncoding]);
        p["SoundLen"] = cocos2d::LuaValue::stringValue(std::to_string(interval));
        if(param){
            p["Url"] = cocos2d::LuaValue::stringValue([param cStringUsingEncoding:NSUTF8StringEncoding]);
        }
        item["Param"] = cocos2d::LuaValue::dictValue(p);
        cocos2d::LuaBridge::getStack()->pushLuaValueDict(item);
        
        cocos2d::LuaBridge::getStack()->executeFunction(1);
#endif*/
		NSMutableDictionary *dic = @{}.mutableCopy;
		NSMutableDictionary *p = @{}.mutableCopy;
		[dic setObject:@"OnRecordStop" forKey:@"Event"];
		[p setObject:bc forKey:@"BCancel"];
		[p setObject:sl forKey:@"SoundLen"];
		if(param){
            [p setObject:param forKey:@"Url"];
        }
		[dic setObject:p forKey:@"Param"];
		
		NSString *rtn = [self dictionaryToJson:dic];
        UnitySendMessage("TFVoiceTool", "PlayFuncMsg", rtn.UTF8String);
    });
}
- (void)onRecordTimeout{
    //NSLog(@"onRecordTimeout");
    [self stopRecord];
    dispatch_async(dispatch_get_main_queue(), ^{
/*#ifdef COCOA2D_LUA
        cocos2d::LuaBridge::pushLuaFunctionById(delegate_);
        cocos2d::LuaValueDict item;
        item["Event"] = cocos2d::LuaValue::stringValue("OnRecordTimeOut");
        cocos2d::LuaBridge::getStack()->pushLuaValueDict(item);
        
        cocos2d::LuaBridge::getStack()->executeFunction(1);
#endif*/
		NSMutableDictionary *dic = @{}.mutableCopy;
		[dic setObject:@"OnRecordTimeOut" forKey:@"Event"];
		NSString *rtn = [self dictionaryToJson:dic];
        UnitySendMessage("TFVoiceTool", "PlayFuncMsg", rtn.UTF8String);
    });
}
- (void)onRecordFailed:(NSString *)failureInfoString{
    dispatch_async(dispatch_get_main_queue(), ^{
/*#ifdef COCOA2D_LUA
        cocos2d::LuaBridge::pushLuaFunctionById(delegate_);
        cocos2d::LuaValueDict item;
        item["Event"] = cocos2d::LuaValue::stringValue("OnRecordFailed");
        cocos2d::LuaBridge::getStack()->pushLuaValueDict(item);
        
        cocos2d::LuaBridge::getStack()->executeFunction(1);
#endif*/
		NSMutableDictionary *dic = @{}.mutableCopy;
		[dic setObject:@"OnRecordTimeOut" forKey:@"Event"];
		NSString *rtn = [self dictionaryToJson:dic];
        UnitySendMessage("TFVoiceTool", "PlayFuncMsg", rtn.UTF8String);
    });
}

- (void)sendFile:(TFSendTask*)task{
    if (!self.http_manager_up_) {
        self.http_manager_up_ = [AFHTTPSessionManager manager];
        self.http_manager_up_.responseSerializer = [AFJSONResponseSerializer serializer];
    }
    [self.http_manager_up_ POST:[_config_ valueForKey:@"url"] parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        [formData appendPartWithFileData:[NSData dataWithContentsOfFile: task.filePath]
                                    name:@"upfile"
                                fileName:@"upfile.spx"
                                mimeType:@"application/octet-stream"
         ];
        
        [formData appendPartWithFormData:[[self.config_ valueForKey:@"userid"] dataUsingEncoding:NSUTF8StringEncoding]
                                    name:@"userid"];
        [formData appendPartWithFormData:[[self.config_ valueForKey:@"token"] dataUsingEncoding:NSUTF8StringEncoding]
                                    name:@"token"];
        [formData appendPartWithFormData:[[self.config_ valueForKey:@"appid"] dataUsingEncoding:NSUTF8StringEncoding]
                                    name:@"appid"];
        [formData appendPartWithFormData:[[self.config_ valueForKey:@"roomid"] dataUsingEncoding:NSUTF8StringEncoding]
                                    name:@"roomid"];
        [formData appendPartWithFormData:[[NSString stringWithFormat:@"%f",[task.date timeIntervalSince1970]] dataUsingEncoding:NSUTF8StringEncoding]
                                    name:@"record_time"];
        [formData appendPartWithFormData:[[NSString stringWithFormat:@"%.3f",task.interval] dataUsingEncoding:NSUTF8StringEncoding]
                                    name:@"record_len"];
        
        
        // etc.
    } progress:nil success:^(NSURLSessionDataTask *t, id responseObject) {
        if (responseObject) {
            NSString* rsp_url = [NSString stringWithFormat:@"%@?recorder_id=%@&appid=%@&roomid=%@&record_time=%f&record_len=%.3f",
                                 [responseObject valueForKey:@"url"],
                                 [self.config_ valueForKey:@"userid"],
                                 [self.config_ valueForKey:@"appid"],
                                 [self.config_ valueForKey:@"roomid"],
                                 [task.date timeIntervalSince1970],
                                 task.interval];
            NSLog(@"up_file rsp_url = %@",rsp_url);
            [self doDelegateOnRecordStop:rsp_url soundTime:task.interval cancel:NO];
        }else{
            [self doDelegateOnRecordStop:nil soundTime:task.interval cancel:NO];
        }
        @synchronized (self.send_file_list_) {
            [self.send_file_list_ removeObjectAtIndex:0];
            if ([self.send_file_list_ count]>0) {
                [self sendFile:[self.send_file_list_ firstObject]];
            }
        }
    } failure:^(NSURLSessionDataTask *t, NSError *error) {
        //NSLog(@"Error: %@", error);
        [self doDelegateOnRecordStop:nil soundTime:task.interval cancel:NO];
        @synchronized (self.send_file_list_) {
            [self.send_file_list_ removeObjectAtIndex:0];
            if ([self.send_file_list_ count]>0) {
                [self sendFile:[self.send_file_list_ firstObject]];
            }
        }
    }];
}

////////////////player delegate///////////
/*#ifdef COCOA2D_LUA
cocos2d::LuaValueDict convert_from_dic(NSDictionary* dic){
    cocos2d::LuaValueDict result;
    for (NSString* key in [dic allKeys]) {
        result[[key cStringUsingEncoding:NSUTF8StringEncoding]]=cocos2d::LuaValue::stringValue([[dic objectForKey:key] cStringUsingEncoding:NSUTF8StringEncoding]);
    }
    return result;
}
#endif*/
-(void)onPlayover:(NSString*)filename{
    
    NSDictionary* old = nil;
    @synchronized (self.voice_list_) {
        //NSFileManager *fm=[NSFileManager defaultManager];
        //[fm removeItemAtPath:filename error:nil];
        
        for (int i = 0; i< [self.voice_list_ count]; i++) {
            NSDictionary*dic = [self.voice_list_ objectAtIndex:i];
            if ([dic objectForKey:filename]) {
                [self.voice_list_ removeObjectAtIndex:i];
                old = dic;
                break;
            }
        }
    }
    if (!old){
        @synchronized (self.white_voice_list_) {
            for (int i = 0; i< [self.white_voice_list_ count]; i++) {
                NSDictionary*dic = [self.white_voice_list_ objectAtIndex:i];
                if ([dic objectForKey:filename]) {
                    old = dic;
                    break;
                }
            }
        }
    }
    if(old){
        //[self replaceWhiteVoice:old];
    }else{
        NSFileManager *fm=[NSFileManager defaultManager];
        [fm removeItemAtPath:filename error:nil];
    }
}
- (void)onWillplay:(NSString*)filename{
    __block NSString* file =nil;//= [self.voice_list_ objectForKey:filename];
    NSUInteger count = 0;
    @synchronized (self.voice_list_) {
        for (int i = 0; i< [self.voice_list_ count]; i++) {
            NSDictionary*dic = [self.voice_list_ objectAtIndex:i];
            file = [dic objectForKey:filename];
            if (file) {
                [self replaceWhiteVoice:dic];
                break;
            }
        }
        if (file&&[self.voice_list_ count]>0) {
            count = [self.voice_list_ count]-1;
        }else{
            count = [self.voice_list_ count];
        }
    }
    NSLog(@"will count = %d",count);
    if (!file) {
        @synchronized (self.white_voice_list_) {
            for (int i = 0; i< [self.white_voice_list_ count]; i++) {
                NSDictionary*dic = [self.white_voice_list_ objectAtIndex:i];
                file = [dic objectForKey:filename];
                if (file) {
                    break;
                }
            }
        }
    }
    if (!file) {
        [self passPlay];
        return;
    }
    dispatch_async(dispatch_get_main_queue(), ^{
        
        NSURL* url = [NSURL URLWithString:file];
        NSLog(@"willplay url=%@   -----%@",url,[url query]);
        NSDictionary* params = [self dictionaryFromQuery:[url query]];
        NSDictionary* dic = @{
                              @"userid":[NSString stringWithFormat:@"%@",[params valueForKey:@"recorder_id"]],
                              @"appid":[NSString stringWithFormat:@"%@",[params valueForKey:@"appid"]],
                              @"roomid":[NSString stringWithFormat:@"%@",[params valueForKey:@"roomid"]],
                              @"record_len":[NSString stringWithFormat:@"%@",[params valueForKey:@"record_len"]],
                              @"remain_count":[NSString stringWithFormat:@"%lu",(unsigned long)count]
                              };
/*#ifdef COCOA2D_LUA
        cocos2d::LuaBridge::pushLuaFunctionById(delegate_);
        cocos2d::LuaValueDict item;
        item["Event"] = cocos2d::LuaValue::stringValue("OnWillPlay");
        item["Param"] = cocos2d::LuaValue::dictValue(convert_from_dic(dic));
        cocos2d::LuaBridge::getStack()->pushLuaValueDict(item);
        
        cocos2d::LuaBridge::getStack()->executeFunction(1);
#endif*/
		NSMutableDictionary *pdic = @{}.mutableCopy;
		[pdic setObject:@"OnWillPlay" forKey:@"Event"];
		[pdic setObject:dic forKey:@"Param"];
		
		NSString *rtn = [self dictionaryToJson:pdic];
        UnitySendMessage("TFVoiceTool", "PlayFuncMsg", rtn.UTF8String);
    });
}
- (void)onWillstop{
    /*@synchronized (self.voice_list_) {
        NSFileManager *fm=[NSFileManager defaultManager];
        for (NSDictionary* dic in _voice_list_) {
            [fm removeItemAtPath:[[dic allKeys] objectAtIndex:0] error:nil];
        }
        [_voice_list_ removeAllObjects];
    }*/
    dispatch_async(dispatch_get_main_queue(), ^{
        //dispatch_async(_queue_, ^{
/*#ifdef COCOA2D_LUA
        cocos2d::LuaBridge::pushLuaFunctionById(delegate_);
        cocos2d::LuaValueDict item;
        item["Event"] = cocos2d::LuaValue::stringValue("OnPlayStop");
        cocos2d::LuaBridge::getStack()->pushLuaValueDict(item);
        
        cocos2d::LuaBridge::getStack()->executeFunction(1);
#endif*/
		NSMutableDictionary *dic = @{}.mutableCopy;
		[dic setObject:@"OnPlayStop" forKey:@"Event"];
		NSString *rtn = [self dictionaryToJson:dic];
        UnitySendMessage("TFVoiceTool", "PlayFuncMsg", rtn.UTF8String);
        //});
    });
}

////////////////////////////////////////////////

-(void)replaceWhiteVoice:(NSDictionary*)new_dic{
    NSURL *new_url = [NSURL URLWithString:[[new_dic allValues] firstObject]];
    NSDictionary* new_param = [self dictionaryFromQuery:[new_url query]];
    NSString* uid = [new_param objectForKey:@"recorder_id"];
    NSDictionary* dic = nil;
    @synchronized (self.white_voice_list_) {
        Boolean bHave = NO;
        for (int i = 0;i< [self.white_voice_list_ count];i++) {
            dic = [self.white_voice_list_ objectAtIndex:i];
            //NSLog(@"self.white_voice_list_  current dic = %@",dic);
            NSString * net_url = [[dic allValues] firstObject];
            NSURL* url = [NSURL URLWithString:net_url];
            NSDictionary* params = [self dictionaryFromQuery:[url query]];
            if ([[params objectForKey:@"recorder_id"] isEqualToString:uid]) {
                bHave = YES;
                if( ![[[new_dic allKeys] firstObject] isEqualToString:[[dic allKeys] firstObject]]){
                    NSFileManager *fm=[NSFileManager defaultManager];
                    [fm removeItemAtPath:[[dic allKeys] firstObject] error:nil];
                    [self.white_voice_list_ removeObjectAtIndex:i];
                    [self.white_voice_list_ addObject:new_dic];
                }
                
                break;
            }
        }
        if (!bHave) {
            [self.white_voice_list_ addObject:new_dic];
        }
        //NSLog(@"self.white_voice_list_ %@",self.white_voice_list_);
    }
}

static TFAudioManager* manager_;
+ (TFAudioManager *)sharedManager {
    @synchronized(self) {
        if (manager_ == nil){
            manager_ = [[TFAudioManager alloc] init];
            NSFileManager *fileManager = [NSFileManager defaultManager];
            [fileManager removeItemAtPath:[TFAudioManager defaultFilePath] error:nil];
        }
    }
    return manager_;
}

+(void)cleanPlaysCache{
    [[TFAudioManager sharedManager] cleanPlaysCache];
}
+(void)playOneLast:(NSDictionary*)one{
    [[TFAudioManager sharedManager] playOneLast:one];
}
-(void)initAudioSession{
    OSStatus error =0;
    
    UInt32 category = kAudioSessionCategory_PlayAndRecord;
    error = AudioSessionSetProperty(kAudioSessionProperty_AudioCategory, sizeof(category), &category);
    if (error) printf("couldn't set audio category!");
    
    //error = AudioSessionAddPropertyListener(kAudioSessionProperty_AudioRouteChange, propListener, (__bridge void *)self);
    if (error) printf("ERROR ADDING AUDIO SESSION PROP LISTENER! %d\n", (int)error);
    //UInt32 inputAvailable = 0;
    //UInt32 size = sizeof(inputAvailable);
    
    // we do not want to allow recording if input is not available
    //error = AudioSessionGetProperty(kAudioSessionProperty_AudioInputAvailable, &size, &inputAvailable);
    //if (error) printf("ERROR GETTING INPUT AVAILABILITY! %d\n", (int)error);
    
    // we also need to listen to see if input availability changes
    //error = AudioSessionAddPropertyListener(kAudioSessionProperty_AudioInputAvailable, propListener, (__bridge void *)self);
    if (error) printf("ERROR ADDING AUDIO SESSION PROP LISTENER! %d\n", (int)error);
    if ([self isHeadsetPluggedIn]) {
        [[AVAudioSession sharedInstance] overrideOutputAudioPort:AVAudioSessionPortOverrideNone error:nil];
    }else{
        [[AVAudioSession sharedInstance] overrideOutputAudioPort:AVAudioSessionPortOverrideSpeaker error:nil];
    }
    error = AudioSessionSetActive(true);
    if (error) printf("AudioSessionSetActive (true) failed");
    
}
- (BOOL)isHeadsetPluggedIn {
    AVAudioSessionRouteDescription* route = [[AVAudioSession sharedInstance] currentRoute];
    for (AVAudioSessionPortDescription* desc in [route outputs]) {
        if ([[desc portType] isEqualToString:AVAudioSessionPortHeadphones])
            return YES;
    }
    return NO;
}
- (void)sensorStateChange:(NSNotification *)notification {
    //如果此时手机靠近面部放在耳朵旁，那么声音将通过听筒输出，并将屏幕变暗
    if ([[UIDevice currentDevice] proximityState] == YES) {
        //NSLog(@"Device is close to user");
        if ([self isHeadsetPluggedIn]) {
            [[AVAudioSession sharedInstance] overrideOutputAudioPort:AVAudioSessionPortOverrideNone error:nil];
        }else{
            [[AVAudioSession sharedInstance] overrideOutputAudioPort:AVAudioSessionPortOverrideSpeaker error:nil];
        }
    }
    else {
        //NSLog(@"Device is not close to user");
        [[AVAudioSession sharedInstance] overrideOutputAudioPort:AVAudioSessionPortOverrideNone error:nil];
    }
}

- (void)startProximityMonitering {
    [[UIDevice currentDevice] setProximityMonitoringEnabled:YES];
    //NSLog(@"开启距离监听");
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sensorStateChange:)
                                                 name:@"UIDeviceProximityStateDidChangeNotification"
                                               object:nil];
}

- (void)stopProximityMonitering {
    [[AVAudioSession sharedInstance] setCategory:AVAudioSessionCategoryPlayAndRecord error:nil];
    [[UIDevice currentDevice] setProximityMonitoringEnabled:NO];
    //NSLog(@"关闭距离监听");
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}
void interruptionListener(	void *	inClientData,
                          UInt32	inInterruptionState)
{
    TFAudioManager *THIS = (__bridge TFAudioManager*)inClientData;
    THIS.inInterruptionState_ = inInterruptionState;
    if (inInterruptionState == kAudioSessionBeginInterruption)
    {
       // NSLog(@"interruptionListener  will stop");
        
        if (!THIS.bStopRecord_){
            [THIS cancelRecord];
        }else{
            dispatch_async(THIS.queue_, ^{
                [THIS.player_ stop];
            });
        }
    }else{
        [THIS initAudioSession];
    }
}
/*
void propListener(	void *                  inClientData,
                  AudioSessionPropertyID	inID,
                  UInt32                  inDataSize,
                  const void *            inData)
{
    TFAudioMenager *THIS = (__bridge TFAudioMenager*)inClientData;
    if (inID == kAudioSessionProperty_AudioRouteChange)
    {
        CFDictionaryRef routeDictionary = (CFDictionaryRef)inData;
        //CFShow(routeDictionary);
        CFNumberRef reason = (CFNumberRef)CFDictionaryGetValue(routeDictionary, CFSTR(kAudioSession_AudioRouteChangeKey_Reason));
        SInt32 reasonVal;
        CFNumberGetValue(reason, kCFNumberSInt32Type, &reasonVal);
        if (reasonVal != kAudioSessionRouteChangeReason_CategoryChange)
        {
            // stop the queue if we had a non-policy route change
            NSLog(@"propListener will stop");
            //[THIS stopPlay];
            //[THIS stopRecord];
        }
    }
    else if (inID == kAudioSessionProperty_AudioInputAvailable)
    {
        if (inDataSize == sizeof(UInt32))
        {
            
        }
    }
}
*/
@end

#if defined(__cplusplus)
extern "C" {
#endif
    extern void UnitySendMessage(const char* obj, const char* method, const char* msg);
	
	void startRecord(const char* sParams)
	{
		NSString *params =[NSString stringWithUTF8String:sParams];
        NSDictionary *config =  [[TFAudioManager sharedManager] dictionaryWithJsonString:params];
		[[TFAudioManager sharedManager] startRecord:config];
	}
	void stopRecord()
	{
		[[TFAudioManager sharedManager] stopRecord];
	}
	void cancelRecord()
	{
		[[TFAudioManager sharedManager] cancelRecord];
	}

	void addPlay(const char* sParams)
	{
		NSString *params =[NSString stringWithUTF8String:sParams];
		NSDictionary *file =  [[TFAudioManager sharedManager] dictionaryWithJsonString:params];
		[[TFAudioManager sharedManager] addPlay:file];
	}
	
	void passPlay(){
		[[TFAudioManager sharedManager] passPlay];
	}
	
	void stopPlay()
	{
		[[TFAudioManager sharedManager] stopPlay:YES];
	}
    

#if defined(__cplusplus)
}
#endif
