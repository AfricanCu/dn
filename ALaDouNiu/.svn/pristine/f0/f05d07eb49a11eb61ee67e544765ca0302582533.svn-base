//
//  TaiJiShieldManager.m
//  thjPaoDekuai_app
//
//  Created by toudada on 2017/8/22.
//
//

#import "TaiJiShieldManager.h"
#import "guandu_oc.h"

@implementation TaiJiShieldManager

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


- (void)taiJiShieldAction:(NSDictionary *)dic{
    
    NSString *serverIPAddr = dic[@"serverIPAddr"];
    int servePort = [dic[@"serverPort"] intValue];
    int protocol = [dic[@"protocol"] intValue];
    
    NSLog(@"端口---servePort = %d",servePort);
    NSLog(@"别名===serverIPAddr = %@",serverIPAddr);
    
    
    dispatch_queue_t queue = dispatch_queue_create("q1", DISPATCH_QUEUE_CONCURRENT);
    
    dispatch_async(queue, ^{
        
        NSString *serverIP;
        int serverPort;
        
     int i = [Guandu getSecurityServerIP:&serverIP getSecurityServerPort:&serverPort setOriginalServerIPAddr:serverIPAddr setOriginalServerPort:servePort setOriginalProtocol:protocol];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            
            if(i == 1){
                NSNumber *number = [NSNumber numberWithInt:i];
                NSNumber *myport = [NSNumber numberWithInt:serverPort];
                NSLog(@"%d",i);
                NSLog(@"serverIP =>>>>>>>%@",serverIP);
                NSLog(@"serverPort =>>>>>>%d",serverPort);
                
				NSMutableDictionary *rtndic = @{}.mutableCopy;
				[rtndic setObject:number forKey:@"code"];
				[rtndic setObject:serverIP forKey:@"serverIP"];
				[rtndic setObject:myport forKey:@"serverPort"];
				NSString *rtn = [self dictionaryToJson:rtndic];
				UnitySendMessage("LocationTool", "TaiJiDunRtn", rtn.UTF8String);
                
            }else{
                NSNumber *number = [NSNumber numberWithInt:i];
                NSMutableDictionary *rtndic = @{}.mutableCopy;
				[rtndic setObject:number forKey:@"code"];
				NSString *rtn = [self dictionaryToJson:rtndic];
				UnitySendMessage("LocationTool", "TaiJiDunRtn", rtn.UTF8String);
                
            }
            
        });
        
 
    });
	
}

static dispatch_once_t onceToken;
static TaiJiShieldManager *instance;
+(TaiJiShieldManager*) shareInstance{
	dispatch_once(&onceToken, ^{
		instance = [[TaiJiShieldManager alloc] init];
	});
	return instance;
}


@end

#if defined(__cplusplus)
extern "C" {
#endif
    extern void UnitySendMessage(const char* obj, const char* method, const char* msg);
	
	void taiJiDun_Unity(const char* sParams)
	{
		NSString *params =[NSString stringWithUTF8String:sParams];
        NSDictionary *config =  [[TaiJiShieldManager sharedManager] dictionaryWithJsonString:params];
		[[TaiJiShieldManager sharedManager] taiJiShieldAction:config];
	}
	

#if defined(__cplusplus)
}
#endif

