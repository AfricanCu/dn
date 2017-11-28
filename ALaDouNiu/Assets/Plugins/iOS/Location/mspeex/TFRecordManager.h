//
//  TFRecorddManager.h
//  TengFei
//
//  Created by Jenuce on 2016/11/14.
//  Copyright © 2016年 TengFei. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol TFRecordingDelegate <NSObject>
- (void)onRecordStart;
- (void)onRecordStop:(NSString *)filePath startDate:(NSDate*)date soundTime:(NSTimeInterval)interval cancel: (BOOL)bCancel;
- (void)onRecordTimeout;
- (void)onRecordFailed:(NSString *)failureInfoString;

//@optional
//- (void)levelMeterChanged:(float)levelMeter;

@end

@interface TFRecordManager : NSObject
@property (nonatomic, assign)  id<TFRecordingDelegate> delegate;

- (void)start:(NSString*)filename;
- (void)stop;
-(void) cancel;

@end
