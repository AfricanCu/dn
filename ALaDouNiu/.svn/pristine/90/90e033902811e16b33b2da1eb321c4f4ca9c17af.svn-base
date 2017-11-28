//
//  AQPlayer.m
//  TengFei
//
//  Created by Jenuce on 2016/11/15.
//  Copyright © 2016年 TengFei. All rights reserved.
//

#import "AQPlayer.h"
#import <AudioToolbox/AudioToolbox.h>
#import <AudioToolbox/AudioFile.h>
#define QUEUE_BUFFER_SIZE 4 //队列缓冲个数
#define MIN_SIZE_PER_FRAME 320 //每侦最小数据长

@interface AQPlayer(){
    AudioStreamBasicDescription audio_description_;///音频参数
    AudioQueueRef audio_queue_;//音频播放队列
    AudioQueueBufferRef audio_queue_buffers_[QUEUE_BUFFER_SIZE];//音频缓存
    NSMutableData *pcm_data_;
    
    bool need_playover_;
}
@end


@implementation AQPlayer
@synthesize bstop;
@synthesize delegate_=delegate;
- (id)init{
    self = [super init];
    bstop = true;
    pcm_data_ = [NSMutableData data];
    need_playover_ = true;
    return self;
}
- (void)dealloc{
    OSStatus result = 0;
    for (int i = 0; i < QUEUE_BUFFER_SIZE; ++i) {
        result = AudioQueueFreeBuffer(audio_queue_, audio_queue_buffers_[i]);
        audio_queue_buffers_[i] = NULL;
    }
    
}
- (void)start{
    if (bstop) {
        bstop = false;
        @synchronized (pcm_data_){
            need_playover_ = true;
        }
        [self initAudio];
        OSStatus status = AudioQueueStart(audio_queue_, NULL);
        if (status && [delegate respondsToSelector:@selector(playerError:)]) {
            [delegate playerError:[NSString stringWithFormat:@"start error[%d]",status]];
        }
    }
}
- (void)pass{
    if (!bstop) {
        @synchronized (pcm_data_){
            [pcm_data_ setData:[NSData data]];
            need_playover_ = true;
        }
    }
}
- (void)pause{
}
- (void)stop{
    if (bstop) {
        return;
    }
    @synchronized (pcm_data_){
        bstop = true;
        need_playover_ = false;
        [pcm_data_ setData:[NSData data]];
    }
    OSStatus status = AudioQueueStop(audio_queue_,true);
    if (status && [delegate respondsToSelector:@selector(playerError:)]) {
        [delegate playerError:[NSString stringWithFormat:@"stop error[%d]",status]];
    }
    AudioQueueDispose(audio_queue_, YES);
    @synchronized (pcm_data_) {
        if (self.delegate_) {
            [self.delegate_ playover];
        }
    }
}
-(void)initAudio {
    ///设置音频参数
    audio_description_.mSampleRate = 8000;//采样率
    audio_description_.mFormatID = kAudioFormatLinearPCM;
    audio_description_.mFormatFlags = kLinearPCMFormatFlagIsSignedInteger | kAudioFormatFlagIsPacked;
    audio_description_.mChannelsPerFrame = 1;///单声道
    audio_description_.mFramesPerPacket = 1;//每一个packet一侦数据
    audio_description_.mBitsPerChannel = 16;//每个采样点16bit量化
    audio_description_.mBytesPerFrame = (audio_description_.mBitsPerChannel/8) * audio_description_.mChannelsPerFrame;
    audio_description_.mBytesPerPacket = audio_description_.mBytesPerFrame ;
    ///创建一个新的从audioqueue到硬件层的通道
    //  AudioQueueNewOutput(&audioDescription, AudioPlayerAQInputCallback, self, CFRunLoopGetCurrent(), kCFRunLoopCommonModes, 0, &audioQueue);///使用当前线程播
    OSStatus result = AudioQueueNewOutput(&audio_description_, AudioPlayerAQInputCallback, (__bridge void *)(self), nil, nil, 0, &audio_queue_);//使用player的内部线程播
    
    ////添加buffer区
    if (result == 0) {
        for(int i=0;i<QUEUE_BUFFER_SIZE;i++) {
            AudioQueueAllocateBuffer(audio_queue_, MIN_SIZE_PER_FRAME, &audio_queue_buffers_[i]);///创建buffer区，MIN_SIZE_PER_FRAME为每一侦所需要的最小的大小，该大小应该比每次往buffer里写的最大的一次还大
            memset(audio_queue_buffers_[i]->mAudioData, 0, MIN_SIZE_PER_FRAME);
            audio_queue_buffers_[i]->mAudioDataByteSize = MIN_SIZE_PER_FRAME;
            if (AudioQueueEnqueueBuffer(audio_queue_, audio_queue_buffers_[i], 0, NULL)>0){
                break;
            }
        }
    }
}
- (void)putMediaData:(NSData*)data{
    if (bstop) {
        return;
    }
    @synchronized (pcm_data_) {
        [pcm_data_ appendData:data];
        need_playover_ = true;
    }
}
- (void)fillBuffer:(AudioQueueBufferRef)buffer {
    @synchronized (pcm_data_){
        if ([pcm_data_ length]>=MIN_SIZE_PER_FRAME) {
            memcpy(buffer->mAudioData, [pcm_data_ bytes], MIN_SIZE_PER_FRAME);
            pcm_data_ = [NSMutableData dataWithBytesNoCopy:(Byte *)[pcm_data_ bytes]+MIN_SIZE_PER_FRAME length:[pcm_data_ length] - MIN_SIZE_PER_FRAME freeWhenDone:NO];
        }else if ([pcm_data_ length]>0){
            memcpy(buffer->mAudioData, [pcm_data_ bytes], [pcm_data_ length]);
            [pcm_data_ setData:[NSData data]];
            if (self.delegate_ &&need_playover_) {
                [self.delegate_ playover];
            }
            need_playover_ = false;
        }else{
            memset(buffer->mAudioData, 0, MIN_SIZE_PER_FRAME);
            if (self.delegate_ &&need_playover_) {
                [self.delegate_ playover];
            }
            need_playover_ = false;
        }
    AudioQueueEnqueueBuffer(audio_queue_, buffer, 0, NULL);
   }
}

static void AudioPlayerAQInputCallback(void *input, AudioQueueRef outQ, AudioQueueBufferRef outQB) {
    AQPlayer *player = (__bridge AQPlayer *)input;
    [player fillBuffer:outQB];
}

@end
