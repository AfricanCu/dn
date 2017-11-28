//
//  TFAudioManager.h
//  TengFei
//
//  Created by Jenuce on 2016/11/17.
//  Copyright © 2016年 TengFei. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TFAudioManager : NSObject
+ (TFAudioManager *)sharedManager;
+(void)setDelegate:(NSDictionary*)delegate;

+(void)startRecord:(NSDictionary*)config;
+(void)stopRecord;
+(void)cancelRecord;

+(void)addPlay:(NSDictionary*)file;
+(void)passPlay;
+(void)stopPlay;

+(void)cleanPlaysCache;
+(void)playOneLast:(NSDictionary*)one;
@end
