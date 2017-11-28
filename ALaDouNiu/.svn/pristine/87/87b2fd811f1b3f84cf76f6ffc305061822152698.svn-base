//
//  TFRecordManager.mm
//  TengFei
//
//  Created by Jenuce on 2016/11/14.
//  Copyright © 2016年 TengFei. All rights reserved.
//

#import "TFRecordManager.h"

#import "AQRecorder.h"
#import "TFEncapsulator.h"

@interface TFRecordManager ()
@property (nonatomic,assign) AQRecorder *recorder_;

@property (nonatomic,strong) TFEncapsulator *encapsulator_;
@property (nonatomic,assign) AudioQueueLevelMeterState *level_meter_states_;

//@property (nonatomic, strong) NSTimer *timer_level_meter_;
@property (nonatomic, strong) NSTimer *timer_timeout_;
@property (nonatomic, strong) NSDate* record_date_;

@end

@implementation TFRecordManager

- (id)init{
    self = [super init];
    if (self) {
        self.recorder_ = nullptr;
        self.level_meter_states_ = nullptr;
        self.encapsulator_ = [[TFEncapsulator alloc] init];
        self.encapsulator_.delegete = (id)self;
    }
    return self;
}
- (void)dealloc{
    if (_recorder_) {
        delete _recorder_;
        _recorder_ = nullptr;
        free(_level_meter_states_);
        _level_meter_states_ = nullptr;
    }
}


- (void)start:(NSString*)filename{
    //NSLog(@"record start %@",filename);
    if (_recorder_) {
        return;
    }
    if (_recorder_==nullptr) {
        _recorder_ = new AQRecorder();
        self.level_meter_states_ = (AudioQueueLevelMeterState *)malloc(sizeof(AudioQueueLevelMeterState) * 1);        
    }
    self.record_date_ = [NSDate date];
    //self.timer_level_meter_ = [NSTimer scheduledTimerWithTimeInterval:0.1 target:self selector:@selector(updateLevelMeter:) userInfo:nil repeats:YES];
    dispatch_async(dispatch_get_main_queue(), ^{
        self.timer_timeout_ = [NSTimer scheduledTimerWithTimeInterval:30.1f target:self selector:@selector(timeoutCheck:) userInfo:nil repeats:NO];
    });
    [self.encapsulator_ setFileName:filename];
    if (! _recorder_->IsRunning() &&  ! _recorder_->StartRecord(self.encapsulator_)) {
        if ([self.delegate respondsToSelector:@selector(onRecordFailed:)]) {
            [self.delegate onRecordFailed:@"程序错误，无法继续录音，请重启程序试试"];
        }
        return;
    }
    if ([self.delegate respondsToSelector:@selector(onRecordStart)]) {
        [self.delegate onRecordStart];
    }
}
- (void)stop{
    //NSLog(@"record stop");
    if (!_recorder_) {
        return;
    }
    if (_recorder_) {
        _recorder_->StopRecord();
        _recorder_ = nil;
    }
    
    [self.timer_timeout_ invalidate];
    self.timer_timeout_ = nil;
    
    //[self.timer_level_meter_ invalidate];
    
    //self.timer_level_meter_ = nil;
}
-(void) cancel{
    if (!_recorder_) {
        return;
    }
    if (_recorder_) {
        _recorder_->StopRecord(true);
        _recorder_ = nil;
    }
    [self.timer_timeout_ invalidate];
    self.timer_timeout_ = nil;
}

/*
- (void)updateLevelMeter:(id)sender {
    if (self.delegate) {
        UInt32 dataSize = sizeof(AudioQueueLevelMeterState);
        AudioQueueGetProperty(_recorder_->Queue(), kAudioQueueProperty_CurrentLevelMeter, _level_meter_states_, &dataSize);
        if ([self.delegate respondsToSelector:@selector(levelMeterChanged:)]) {
            [self.delegate levelMeterChanged:_level_meter_states_[0].mPeakPower];
        }
        
    }
}
*/
- (void)timeoutCheck:(id)sender {
    if ([self.delegate respondsToSelector:@selector(onRecordTimeout)]) {
        [self.delegate onRecordTimeout];
    }
}
- (void)encapsulatingOver:(NSString*)filename soundTimeLong:(NSTimeInterval)interval cancel: (BOOL)bCancel {
    if ([self.delegate respondsToSelector:@selector(onRecordStop:startDate:soundTime:cancel:)]) {
        [self.delegate onRecordStop:filename startDate:self.record_date_ soundTime:interval cancel:bCancel];
    }
}

@end
