//
//  UnityBetteryManager.m
//  thjPaoDekuai_app
//
//  Created by toudada on 2017/9/21.
//
//

#import "UnityBetteryManager.h"

@implementation UnityBetteryManager

- (NSString*)dictionaryToJson:(NSDictionary *)dic

{
    
    NSError *parseError = nil;
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&parseError];
    
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
}

- (void)battery_Unity{

    int networktType;
    int signalLevel;
    NSMutableDictionary *deviceDic = @{}.mutableCopy;
    UIApplication *app = [UIApplication sharedApplication];
    NSArray *subviews = [[[app valueForKeyPath:@"statusBar"] valueForKeyPath:@"foregroundView"] subviews];
    NSString *dataNetworkItemView = nil;
    for (id subview in subviews) {
        if ([subview isKindOfClass:NSClassFromString(@"UIStatusBarDataNetworkItemView")]) {
            int networkType = [[subview valueForKeyPath:@"dataNetworkType"] intValue];
            switch (networkType) {
                case 0:
                    NSLog(@"NONE");
                    networktType = 0;
                    break;
                case 1:
                    NSLog(@"2G");
                    networktType = 2;
                    break;
                case 2:
                    NSLog(@"3G");
                    networktType = 3;
                    break;
                case 3:
                    NSLog(@"4G");
                    networktType = 4;
                    break;
                case 5:
                {
                    for (id subview in subviews) {
                        if([subview isKindOfClass:[NSClassFromString(@"UIStatusBarDataNetworkItemView") class]]) {
                            dataNetworkItemView = subview;
                            break;
                        }
                    }
                    signalLevel = [[dataNetworkItemView valueForKey:@"_wifiStrengthBars"] intValue];
                    
                    NSLog(@"signalLevel %d", signalLevel);
                    NSLog(@"WIFI信号强度：%d",signalLevel);
                }
                    networktType = 1;
                    NSLog(@"WIFI");
                    break;
                default:
                    break;
            }
        }
    }
    
    //拿到当前设备
    UIDevice * device = [UIDevice currentDevice];
    
    //是否允许监测电池
    //要想获取电池电量信息和监控电池电量 必须允许
    device.batteryMonitoringEnabled = true;
    //1、check
    /*
     获取电池电量
     */
    float level = device.batteryLevel*100;
    NSLog(@"电池剩余比例：%@", [NSString stringWithFormat:@"%0.1f",level*100]);
    
    [deviceDic setObject:[NSNumber numberWithInt:networktType] forKey:@"networktType"];
    [deviceDic setObject:[NSNumber numberWithFloat:level] forKey:@"level"];
    [deviceDic setObject:[NSNumber numberWithInt:100] forKey:@"scale"];
    [deviceDic setObject:[NSNumber numberWithInt:signalLevel] forKey:@"signalLevel"];
    
    [self returnDeviceMessge:deviceDic];
}


- (void)returnDeviceMessge:(NSDictionary *)dic{
	NSString *rtn = [self dictionaryToJson:dic];
	UnitySendMessage("LocationTool", "BatteryRtn", rtn.UTF8String);
}

static dispatch_once_t onceToken;
static UnityBetteryManager *instance;
+(UnityBetteryManager*) shareInstance{
    dispatch_once(&onceToken, ^{
        instance = [[UnityBetteryManager alloc] init];
    });
    return instance;
}

@end

#if defined(__cplusplus)
extern "C" {
#endif
    extern void UnitySendMessage(const char* obj, const char* method, const char* msg);
	
	void battery_Unity()
	{
		[[UnityBetteryManager shareInstance] battery_Unity];
	}
	

#if defined(__cplusplus)
}
#endif
