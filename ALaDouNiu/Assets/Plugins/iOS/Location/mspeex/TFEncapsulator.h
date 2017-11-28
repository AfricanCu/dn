//
//  TFEncapsulator.h
//  TengFei
//
//  Created by Jenuce on 2016/11/14.
//  Copyright © 2016年 TengFei. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol TFEncapsulatingDelegate <NSObject>

- (void)encapsulatingOver:(NSString*)filename soundTimeLong:(NSTimeInterval)interval cancel: (BOOL)bCancel;

@end


@interface TFEncapsulator : NSObject
@property (nonatomic, assign) id<TFEncapsulatingDelegate> delegete;

- (void)setFileName:(NSString *)filename;

//输入新PCM数据。注意数据同步
- (void)inputPCMDataFromBuffer:(Byte *)buffer size:(UInt32)dataSize;

//停止封装。是否是取消，true是取消，否则是正常停止录音
- (void)stopEncapsulating:(BOOL)bCancel;

//为即将开始的封装做准备，包括写入ogg的头
- (void)prepareForEncapsulating;
@end
