//
//  TFPlayMenager.h
//  TengFei
//
//  Created by Jenuce on 2016/11/15.
//  Copyright © 2016年 TengFei. All rights reserved.
//

#import <Foundation/Foundation.h>
@protocol TFPlayingDelegate <NSObject>
- (void)onWillplay:(NSString*)filename;
- (void)onPlayover:(NSString*)filename;
- (void)onWillstop;
@end

@interface TFPlayManager : NSObject
@property (nonatomic, assign)  id<TFPlayingDelegate> delegate;
- (Boolean)isPlaying;

-(void)addToFirst:(NSString*)file;
-(void)addPlayFiles:(NSArray*)array;
-(void)addPlayFile:(NSString*)file;

-(void) pass;
-(void) stop;

@end
