
#import "VLX.h"
#import <VeloxitySDK/VeloxitySDK.h>

@interface VLX () <VeloxityDelegate>

@end

@implementation VLX

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(setLicenseKey:(NSString *)licenseKey)
{
    [[Veloxity sharedInstance] setLicenseKey:licenseKey];
}

RCT_EXPORT_METHOD(setWebServiceUrl:(NSString *)webServiceUrl)
{
    [[Veloxity sharedInstance] setWebServiceUrl:webServiceUrl];
}

RCT_EXPORT_METHOD(setAuthorizationMenu:(NSString*)title withMessage:(NSString*)message andAcceptTitle:(NSString*)acceptTitle andDenyTitle:(NSString*)denyTitle)
{
    [[Veloxity sharedInstance] setDelegate:self];
    [[Veloxity sharedInstance] setAuthorizationMenu:title withMessage:message andAcceptTitle:acceptTitle andDenyTitle:denyTitle];
}

RCT_EXPORT_METHOD(registerDeviceToken:(NSString*)token)
{
    [[Veloxity sharedInstance] registerDeviceTokenPlainString:token];
}

RCT_EXPORT_METHOD(setAskPermission:(BOOL) askPermission)
{
    [[Veloxity sharedInstance] setNeverAskPermissions:!askPermission];
}

RCT_EXPORT_METHOD(sendCustomData:(NSDictionary*)jsonContent)
{
    [[Veloxity sharedInstance] sendCustomData:jsonContent];
}

RCT_EXPORT_METHOD(startBackgroundTransactionWith:(NSDictionary*)userInfo)
{
    [[Veloxity sharedInstance] startBackgroundTransactionWithUserInfo:userInfo];
}

RCT_EXPORT_METHOD(start)
{
    [[Veloxity sharedInstance] setDelegate:self];
    [[Veloxity sharedInstance] start];
}

RCT_EXPORT_METHOD(optIn)
{
    
    [[Veloxity sharedInstance] setDelegate:self];
    [[Veloxity sharedInstance] optIn];
}

RCT_EXPORT_METHOD(optOut)
{
    [[Veloxity sharedInstance] optOut];
}

RCT_EXPORT_METHOD(getServiceStatus:(RCTResponseSenderBlock)callback)
{
    callback(@[[[Veloxity sharedInstance] serviceStatus] == YES ? @YES : @NO]);
}

RCT_EXPORT_METHOD(getDeviceId:(RCTResponseSenderBlock)callback)
{
    callback(@[[[Veloxity sharedInstance] getDeviceId]]);
}

- (void)vlxAuthorizationDidSucceed{
    [self sendEventWithName:@"AuthSucceed" body:nil];
}

- (void)vlxAuthorizationDidFailed{
    [self sendEventWithName:@"AuthFailed" body:nil];
}

- (NSArray<NSString *> *)supportedEvents
{
    return @[@"AuthSucceed",@"AuthFailed"];
}

@end


