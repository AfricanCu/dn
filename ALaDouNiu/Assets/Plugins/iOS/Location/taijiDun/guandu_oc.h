//
//  guandu_oc.h
//  Copyright © 2016年 cloudaemon. All rights reserved.
//
#import <Foundation/Foundation.h>


#ifndef IPPROTO_TCP
#define IPPROTO_TCP 6
#endif

#ifndef IPPROTO_UDP
#define IPPROTO_UDP 17
#endif

#define ERRNO_OK 1
#define ERRNO_HOSTURL_NOT_CONFIG 4
#define ERRNO_NO_INIT 5
#define ERRNO_PARAMETER_ERROR 6
#define ERRNO_SYSTEM_ERROR 7

__attribute__((visibility ("default")))
@interface Guandu : NSObject
+ (void)init;
+ (NSURLSessionConfiguration *)setURLSessionConfiguration: (NSString *)HostURL
                                              setHostPort: (int)HostPort;
+ (int)getSecurityServerIP: (NSString **)ServerIPAddr
      getSecurityServerPort: (int *)ServerPort
    setOriginalServerIPAddr:(NSString *)OriginalServerIPAddr
      setOriginalServerPort:(int)OriginalServerPort
        setOriginalProtocol:(int)Protocol;
@end

