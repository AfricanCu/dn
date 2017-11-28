//
//  AQPlayer.h
//  TengFei
//
//  Created by Jenuce on 2016/11/15.
//  Copyright © 2016年 TengFei. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol AQPlayerDelegate <NSObject>
-(void)playerError:(NSString*)error;
-(void)playover;
@end

@interface AQPlayer : NSObject
@property (nonatomic,assign) id<AQPlayerDelegate> delegate_;
@property (atomic,assign) BOOL bstop;
- (void)start;
- (void)pass;
- (void)pause;
- (void)stop;
- (void)putMediaData:(NSData*)data;
@end
