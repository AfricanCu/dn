//
//  LoctionIPA.m
//  LoctionDome
//
//  Created by toudada on 2017/8/24.
//  Copyright © 2017年 toudada. All rights reserved.
//

#import "LocationIPA.h"
#import <CoreLocation/CoreLocation.h>
#import <UIKit/UIKit.h>
#import "JZLocationConverter.h"


@interface LocationIPA ()<CLLocationManagerDelegate>{

    BOOL isUnAuthorization;
}

@property (nonatomic,strong)CLLocationManager *mgr;
@property (nonatomic,strong)CLGeocoder *geocoder;
@property (nonatomic,assign)int classID;
@property (nonatomic,assign)float longitude;//经度
@property (nonatomic,assign)float latitude;//纬度

@end

/*
 isTrue ---> 0 :获取GPS定位成功
        ---> 1 :用户没有开启权限
        ---> 2 :获取经纬度成功;但是地理反编码失败！
        ---> 3 :其他错误
 */

static dispatch_once_t onceToken;
static LocationIPA *_manager;

@implementation LocationIPA

+ (instancetype)manager{

    dispatch_once(&onceToken, ^{
        
        _manager = [[LocationIPA alloc] init];
        
    });
    
    return _manager;
}





- (NSString*)dictionaryToJson:(NSDictionary *)dic

{
    
    NSError *parseError = nil;
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&parseError];
    
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
}

/**
    请求用户授权
 */
- (void)requestingUserAuthorization{
    
    
    if(!_mgr){
        
        _mgr = [[CLLocationManager alloc] init];
    }
    _mgr.delegate = self;
    if([_mgr respondsToSelector:@selector(requestAlwaysAuthorization)]){
        [_mgr requestAlwaysAuthorization]; // 永久授权
        [_mgr requestWhenInUseAuthorization]; //使用中授权
        
    }
    
    if([CLLocationManager locationServicesEnabled]){
        
//        isUnAuthorization = YES;
        //用户开启授权
         //设置精度
        [_mgr setDesiredAccuracy:kCLLocationAccuracyBest];
        //设置距离筛选
        [_mgr setDistanceFilter:10];
        //开始定位
        [_mgr startUpdatingLocation];
        
        NSTimer *_timer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(timerAction:) userInfo:nil repeats:YES];
        
        [[NSRunLoop currentRunLoop] addTimer:_timer forMode:NSDefaultRunLoopMode];
        
    }
}

- (void)timerAction:(NSTimer *)timer{

    
    NSLog(@"执行");
    
    NSLog(@"------>经度：%0.4f,纬度：%0.4f<--------",_longitude,_latitude);
    
    if(_longitude == 0 || _latitude == 0){
    
        if(!isUnAuthorization){
            
            NSLog(@"获取地理位置失败！！！");
            NSDictionary *dic = @{@"isTrue":@3};
            [self returnsTheUserAddress:dic];
            
        }
        
    }else{
    
        [self getAddressByLatitude:_latitude longitude:_longitude];
        
    }
    
    [_mgr stopUpdatingLocation];
    [timer invalidate];
}

/**
 定位成功回调
 */
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations{
    
    
    CLLocation *lo =[locations lastObject];

    if(lo.horizontalAccuracy >0){
        
        //获取经纬度有效
        CLLocationCoordinate2D cloc2D1 = [JZLocationConverter wgs84ToGcj02:lo.coordinate];
        
        if(cloc2D1.latitude != 0 && cloc2D1.longitude != 0)
            
        _latitude = cloc2D1.latitude;
        _longitude = cloc2D1.longitude;
        
        NSLog(@"经度：%0.4f,纬度：%0.4f",_longitude,_latitude);
    }

}


-(void)getAddressByLatitude:(CLLocationDegrees)latitude longitude:(CLLocationDegrees)longitude{
    
    _geocoder = [[CLGeocoder alloc] init];
    //反地理编码
    CLLocation *location=[[CLLocation alloc]initWithLatitude:latitude longitude:longitude];
    [_geocoder reverseGeocodeLocation:location completionHandler:^(NSArray *placemarks, NSError *error) {
        
        NSInteger code = error.code;
        
        NSLog(@"code = .........%ld",(long)code);
        
        if(error != nil || code != 0){
            
            NSLog(@"地理反编码失败：error = %@",error);
            
            NSDictionary *dic = @{@"isTrue":@2};
            [self returnsTheUserAddress:dic];
            
            return ;
        }
        
        CLPlacemark *placemark=[placemarks firstObject];
        NSArray *formattedAddressLines = [placemark.addressDictionary objectForKey:@"FormattedAddressLines"];
        NSString *add = [formattedAddressLines lastObject];
        NSLog(@"add___________:%@",add);
        
        NSMutableDictionary *dic = @{}.mutableCopy;
        
        NSNumber* latiTude = [NSNumber numberWithFloat:latitude];
        NSNumber* longTtude = [NSNumber numberWithFloat:longitude];
        
        [dic setObject:latiTude forKey:@"latitude"];
        [dic setObject:longTtude forKey:@"longitude"];
        
        if(add.length > 0){
            
            [dic setObject:add forKey:@"address"];//详细地址
            
        }else{
            [dic setObject:@"" forKey:@"address"];
        }
        if(placemark.country.length > 0){
            [dic setObject:placemark.country forKey:@"country"]; //国家
        }else{
            [dic setObject:@"" forKey:@"country"];
        }
        if(placemark.administrativeArea.length > 0){
            [dic setObject:placemark.administrativeArea forKey:@"province"]; //省份
        }else{
            [dic setObject:@"" forKey:@"province"]; //省份
        }
        if(placemark.locality.length > 0){
            [dic setObject:placemark.locality forKey:@"city"]; //城市
        }else{
            [dic setObject:@"" forKey:@"city"]; //城市
        }
        if(placemark.subLocality.length > 0){
            [dic setObject:placemark.subLocality forKey:@"district"]; //地区
        }else{
            [dic setObject:@"" forKey:@"district"]; //地区
        }
        if(placemark.thoroughfare.length > 0){
            [dic setObject:placemark.thoroughfare forKey:@"street"]; //街道
        }else{
            [dic setObject:@"" forKey:@"street"]; //街道
        }
        if(placemark.subThoroughfare.length > 0){
            [dic setObject:placemark.subThoroughfare forKey:@"houseNumber"]; //门牌号
        }else{
            [dic setObject:@"" forKey:@"houseNumber"]; //门牌号
        }
        
        [dic setObject:@0 forKey:@"isTrue"];
        
        NSLog(@"dic = %@",dic);
        [self returnsTheUserAddress:dic];
    }];
}

- (void)returnsTheUserAddress:(NSDictionary *)dic{
    
    int isTrue = [dic[@"isTrue"] intValue];
    
    if(isTrue == 0){
    
        float latitude = [dic[@"latitude"] floatValue];
        NSLog(@"-----latitude = %f",latitude);
        float longitude = [dic[@"longitude"] floatValue];
        NSLog(@"-----longitude = %f",longitude);
        NSString *country = dic[@"country"];
        NSString *province = dic[@"province"];
        NSString *city = dic[@"city"];
        NSString *district = dic[@"district"];
        NSString *address = dic[@"address"];
        NSLog(@"address = @@@@@@@%@",address);
        NSString *street = dic[@"street"];
        NSString *houseNumber = dic[@"houseNumber"];
        
        NSString *rtn = [self dictionaryToJson:dic];
        UnitySendMessage("LocationTool", "ReqLocRtn", rtn.UTF8String);
    }else{
    
        
    }

}


- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{ //此方法为定位失败的时候调用。并且由于会在失败以后重新定位，所以必须在末尾停止更新
    
    NSLog(@"定位失败");

    if(error.code == kCLErrorLocationUnknown)
    {
        NSLog(@"目前位置是未知的");
    }
    else if(error.code == kCLErrorNetwork)
    {
        NSLog(@"一般情况下，网络相关的错误");
    }
    else if(error.code == kCLErrorDenied)
    {
        
        isUnAuthorization = YES;
        NSDictionary *dic = @{@"isTrue":@1};
        [self returnsTheUserAddress:dic];
        
        NSLog(@"获取用户位置范围被拒绝");
        
        UIAlertController *alertController= [UIAlertController alertControllerWithTitle:nil message:@"您没有开启定位" preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *action1 = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:nil];
        [alertController addAction:action1];
        
        [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:alertController animated:YES completion:nil];
        
        NSLog(@"Permission to retrieve location is denied.");
    }
    
    [manager stopUpdatingLocation];

}



@end

#if defined(__cplusplus)
extern "C" {
#endif
    extern void UnitySendMessage(const char* obj, const char* method, const char* msg);
    
    void requestingLocation()
    {
        [[LocationIPA manager] requestingUserAuthorization];
    }
    

#if defined(__cplusplus)
}
#endif



