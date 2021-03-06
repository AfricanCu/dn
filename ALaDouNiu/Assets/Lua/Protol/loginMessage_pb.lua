--Generated By protoc-gen-lua Do not Edit
local protobuf = require "protobuf.protobuf"
module('Protol.loginMessage_pb')

LOGINCM = protobuf.Descriptor();
LOGINCM_UID_FIELD = protobuf.FieldDescriptor();
LOGINCM_SESSIONCODE_FIELD = protobuf.FieldDescriptor();
LOGINCM_LOGINTIME_FIELD = protobuf.FieldDescriptor();
JULEBU = protobuf.Descriptor();
JULEBU_ID_FIELD = protobuf.FieldDescriptor();
JULEBU_NAME_FIELD = protobuf.FieldDescriptor();
JULEBU_MASTERUID_FIELD = protobuf.FieldDescriptor();
JULEBU_MASTERNAME_FIELD = protobuf.FieldDescriptor();
JULEBU_HEADIMG_FIELD = protobuf.FieldDescriptor();
JULEBU_STATE_FIELD = protobuf.FieldDescriptor();
JULEBU_PLAYTYPE_FIELD = protobuf.FieldDescriptor();
JULEBU_NUM_FIELD = protobuf.FieldDescriptor();
LOGINSM = protobuf.Descriptor();
LOGINSM_CODE_FIELD = protobuf.FieldDescriptor();
LOGINSM_NICKNAME_FIELD = protobuf.FieldDescriptor();
LOGINSM_HEADIMG_FIELD = protobuf.FieldDescriptor();
LOGINSM_ROOMID_FIELD = protobuf.FieldDescriptor();
LOGINSM_JOIN_FIELD = protobuf.FieldDescriptor();
LOGINSM_MY_FIELD = protobuf.FieldDescriptor();
LOGINSM_CREATEJ_FIELD = protobuf.FieldDescriptor();
LOGINSM_NOTICE_FIELD = protobuf.FieldDescriptor();
SWLOGINCM = protobuf.Descriptor();
SWLOGINCM_UID_FIELD = protobuf.FieldDescriptor();
SWLOGINCM_SWCODE_FIELD = protobuf.FieldDescriptor();
SWLOGINCM_SID_FIELD = protobuf.FieldDescriptor();
SWLOGINCM_TYPE_FIELD = protobuf.FieldDescriptor();
SWLOGINSM = protobuf.Descriptor();
SWLOGINSM_CODE_FIELD = protobuf.FieldDescriptor();
SWLOGINSM_LOGINSM_FIELD = protobuf.FieldDescriptor();
PLAYERCAST = protobuf.Descriptor();
PLAYERCAST_DIAMOND_FIELD = protobuf.FieldDescriptor();
GAMERECORD = protobuf.Descriptor();
GAMERECORD_NICKNAME_FIELD = protobuf.FieldDescriptor();
GAMERECORD_COIN_FIELD = protobuf.FieldDescriptor();
GAMERECORD_ROUND_FIELD = protobuf.FieldDescriptor();
GAMERECORD_ROOMID_FIELD = protobuf.FieldDescriptor();
GAMERECORD_PLAYTYPE_FIELD = protobuf.FieldDescriptor();
GAMERECORD_TIME_FIELD = protobuf.FieldDescriptor();
GAMERECORD_INDEX_FIELD = protobuf.FieldDescriptor();
GAMERECORD_WINNER_FIELD = protobuf.FieldDescriptor();
ROUND = protobuf.Descriptor();
ROUND_COIN_FIELD = protobuf.FieldDescriptor();
ROUND_TIME_FIELD = protobuf.FieldDescriptor();
ROUND_BANKERSEETINDEX_FIELD = protobuf.FieldDescriptor();
ROUND_BATTLEBACKINDEX_FIELD = protobuf.FieldDescriptor();
GAMERECORDCM = protobuf.Descriptor();
GAMERECORDCM_INDEX_FIELD = protobuf.FieldDescriptor();
GAMERECORDSM = protobuf.Descriptor();
GAMERECORDSM_CODE_FIELD = protobuf.FieldDescriptor();
GAMERECORDSM_ROUND_FIELD = protobuf.FieldDescriptor();
GAMERECORDSM_INDEX_FIELD = protobuf.FieldDescriptor();
PLAYERRECORDCAST = protobuf.Descriptor();
PLAYERRECORDCAST_RECORD_FIELD = protobuf.FieldDescriptor();
PROXYRECORDCAST = protobuf.Descriptor();
PROXYRECORDCAST_RECORD_FIELD = protobuf.FieldDescriptor();
CHARGECAST = protobuf.Descriptor();
CHARGECAST_DIAMOND_FIELD = protobuf.FieldDescriptor();
INVITECODECM = protobuf.Descriptor();
INVITECODECM_INDEX_FIELD = protobuf.FieldDescriptor();
INVITECODESM = protobuf.Descriptor();
INVITECODESM_CODE_FIELD = protobuf.FieldDescriptor();
INVITECODESM_ROUND_FIELD = protobuf.FieldDescriptor();
INVITECODESM_INDEX_FIELD = protobuf.FieldDescriptor();

LOGINCM_UID_FIELD.name = "uid"
LOGINCM_UID_FIELD.full_name = ".msg.LoginMessage.LoginCm.uid"
LOGINCM_UID_FIELD.number = 1
LOGINCM_UID_FIELD.index = 0
LOGINCM_UID_FIELD.label = 1
LOGINCM_UID_FIELD.has_default_value = false
LOGINCM_UID_FIELD.default_value = 0
LOGINCM_UID_FIELD.type = 3
LOGINCM_UID_FIELD.cpp_type = 2

LOGINCM_SESSIONCODE_FIELD.name = "sessionCode"
LOGINCM_SESSIONCODE_FIELD.full_name = ".msg.LoginMessage.LoginCm.sessionCode"
LOGINCM_SESSIONCODE_FIELD.number = 2
LOGINCM_SESSIONCODE_FIELD.index = 1
LOGINCM_SESSIONCODE_FIELD.label = 1
LOGINCM_SESSIONCODE_FIELD.has_default_value = false
LOGINCM_SESSIONCODE_FIELD.default_value = ""
LOGINCM_SESSIONCODE_FIELD.type = 9
LOGINCM_SESSIONCODE_FIELD.cpp_type = 9

LOGINCM_LOGINTIME_FIELD.name = "loginTime"
LOGINCM_LOGINTIME_FIELD.full_name = ".msg.LoginMessage.LoginCm.loginTime"
LOGINCM_LOGINTIME_FIELD.number = 3
LOGINCM_LOGINTIME_FIELD.index = 2
LOGINCM_LOGINTIME_FIELD.label = 1
LOGINCM_LOGINTIME_FIELD.has_default_value = false
LOGINCM_LOGINTIME_FIELD.default_value = ""
LOGINCM_LOGINTIME_FIELD.type = 9
LOGINCM_LOGINTIME_FIELD.cpp_type = 9

LOGINCM.name = "LoginCm"
LOGINCM.full_name = ".msg.LoginMessage.LoginCm"
LOGINCM.nested_types = {}
LOGINCM.enum_types = {}
LOGINCM.fields = {LOGINCM_UID_FIELD, LOGINCM_SESSIONCODE_FIELD, LOGINCM_LOGINTIME_FIELD}
LOGINCM.is_extendable = false
LOGINCM.extensions = {}
JULEBU_ID_FIELD.name = "id"
JULEBU_ID_FIELD.full_name = ".msg.LoginMessage.Julebu.id"
JULEBU_ID_FIELD.number = 1
JULEBU_ID_FIELD.index = 0
JULEBU_ID_FIELD.label = 1
JULEBU_ID_FIELD.has_default_value = false
JULEBU_ID_FIELD.default_value = 0
JULEBU_ID_FIELD.type = 5
JULEBU_ID_FIELD.cpp_type = 1

JULEBU_NAME_FIELD.name = "name"
JULEBU_NAME_FIELD.full_name = ".msg.LoginMessage.Julebu.name"
JULEBU_NAME_FIELD.number = 2
JULEBU_NAME_FIELD.index = 1
JULEBU_NAME_FIELD.label = 1
JULEBU_NAME_FIELD.has_default_value = false
JULEBU_NAME_FIELD.default_value = ""
JULEBU_NAME_FIELD.type = 9
JULEBU_NAME_FIELD.cpp_type = 9

JULEBU_MASTERUID_FIELD.name = "masterUid"
JULEBU_MASTERUID_FIELD.full_name = ".msg.LoginMessage.Julebu.masterUid"
JULEBU_MASTERUID_FIELD.number = 3
JULEBU_MASTERUID_FIELD.index = 2
JULEBU_MASTERUID_FIELD.label = 1
JULEBU_MASTERUID_FIELD.has_default_value = false
JULEBU_MASTERUID_FIELD.default_value = 0
JULEBU_MASTERUID_FIELD.type = 3
JULEBU_MASTERUID_FIELD.cpp_type = 2

JULEBU_MASTERNAME_FIELD.name = "masterName"
JULEBU_MASTERNAME_FIELD.full_name = ".msg.LoginMessage.Julebu.masterName"
JULEBU_MASTERNAME_FIELD.number = 4
JULEBU_MASTERNAME_FIELD.index = 3
JULEBU_MASTERNAME_FIELD.label = 1
JULEBU_MASTERNAME_FIELD.has_default_value = false
JULEBU_MASTERNAME_FIELD.default_value = ""
JULEBU_MASTERNAME_FIELD.type = 9
JULEBU_MASTERNAME_FIELD.cpp_type = 9

JULEBU_HEADIMG_FIELD.name = "headimg"
JULEBU_HEADIMG_FIELD.full_name = ".msg.LoginMessage.Julebu.headimg"
JULEBU_HEADIMG_FIELD.number = 5
JULEBU_HEADIMG_FIELD.index = 4
JULEBU_HEADIMG_FIELD.label = 1
JULEBU_HEADIMG_FIELD.has_default_value = false
JULEBU_HEADIMG_FIELD.default_value = ""
JULEBU_HEADIMG_FIELD.type = 9
JULEBU_HEADIMG_FIELD.cpp_type = 9

JULEBU_STATE_FIELD.name = "state"
JULEBU_STATE_FIELD.full_name = ".msg.LoginMessage.Julebu.state"
JULEBU_STATE_FIELD.number = 6
JULEBU_STATE_FIELD.index = 5
JULEBU_STATE_FIELD.label = 1
JULEBU_STATE_FIELD.has_default_value = false
JULEBU_STATE_FIELD.default_value = 0
JULEBU_STATE_FIELD.type = 5
JULEBU_STATE_FIELD.cpp_type = 1

JULEBU_PLAYTYPE_FIELD.name = "playType"
JULEBU_PLAYTYPE_FIELD.full_name = ".msg.LoginMessage.Julebu.playType"
JULEBU_PLAYTYPE_FIELD.number = 7
JULEBU_PLAYTYPE_FIELD.index = 6
JULEBU_PLAYTYPE_FIELD.label = 1
JULEBU_PLAYTYPE_FIELD.has_default_value = false
JULEBU_PLAYTYPE_FIELD.default_value = ""
JULEBU_PLAYTYPE_FIELD.type = 9
JULEBU_PLAYTYPE_FIELD.cpp_type = 9

JULEBU_NUM_FIELD.name = "num"
JULEBU_NUM_FIELD.full_name = ".msg.LoginMessage.Julebu.num"
JULEBU_NUM_FIELD.number = 8
JULEBU_NUM_FIELD.index = 7
JULEBU_NUM_FIELD.label = 1
JULEBU_NUM_FIELD.has_default_value = false
JULEBU_NUM_FIELD.default_value = 0
JULEBU_NUM_FIELD.type = 5
JULEBU_NUM_FIELD.cpp_type = 1

JULEBU.name = "Julebu"
JULEBU.full_name = ".msg.LoginMessage.Julebu"
JULEBU.nested_types = {}
JULEBU.enum_types = {}
JULEBU.fields = {JULEBU_ID_FIELD, JULEBU_NAME_FIELD, JULEBU_MASTERUID_FIELD, JULEBU_MASTERNAME_FIELD, JULEBU_HEADIMG_FIELD, JULEBU_STATE_FIELD, JULEBU_PLAYTYPE_FIELD, JULEBU_NUM_FIELD}
JULEBU.is_extendable = false
JULEBU.extensions = {}
LOGINSM_CODE_FIELD.name = "code"
LOGINSM_CODE_FIELD.full_name = ".msg.LoginMessage.LoginSm.code"
LOGINSM_CODE_FIELD.number = 1
LOGINSM_CODE_FIELD.index = 0
LOGINSM_CODE_FIELD.label = 1
LOGINSM_CODE_FIELD.has_default_value = false
LOGINSM_CODE_FIELD.default_value = 0
LOGINSM_CODE_FIELD.type = 5
LOGINSM_CODE_FIELD.cpp_type = 1

LOGINSM_NICKNAME_FIELD.name = "nickname"
LOGINSM_NICKNAME_FIELD.full_name = ".msg.LoginMessage.LoginSm.nickname"
LOGINSM_NICKNAME_FIELD.number = 2
LOGINSM_NICKNAME_FIELD.index = 1
LOGINSM_NICKNAME_FIELD.label = 1
LOGINSM_NICKNAME_FIELD.has_default_value = false
LOGINSM_NICKNAME_FIELD.default_value = ""
LOGINSM_NICKNAME_FIELD.type = 9
LOGINSM_NICKNAME_FIELD.cpp_type = 9

LOGINSM_HEADIMG_FIELD.name = "headimg"
LOGINSM_HEADIMG_FIELD.full_name = ".msg.LoginMessage.LoginSm.headimg"
LOGINSM_HEADIMG_FIELD.number = 3
LOGINSM_HEADIMG_FIELD.index = 2
LOGINSM_HEADIMG_FIELD.label = 1
LOGINSM_HEADIMG_FIELD.has_default_value = false
LOGINSM_HEADIMG_FIELD.default_value = ""
LOGINSM_HEADIMG_FIELD.type = 9
LOGINSM_HEADIMG_FIELD.cpp_type = 9

LOGINSM_ROOMID_FIELD.name = "roomId"
LOGINSM_ROOMID_FIELD.full_name = ".msg.LoginMessage.LoginSm.roomId"
LOGINSM_ROOMID_FIELD.number = 4
LOGINSM_ROOMID_FIELD.index = 3
LOGINSM_ROOMID_FIELD.label = 1
LOGINSM_ROOMID_FIELD.has_default_value = false
LOGINSM_ROOMID_FIELD.default_value = ""
LOGINSM_ROOMID_FIELD.type = 9
LOGINSM_ROOMID_FIELD.cpp_type = 9

LOGINSM_JOIN_FIELD.name = "join"
LOGINSM_JOIN_FIELD.full_name = ".msg.LoginMessage.LoginSm.join"
LOGINSM_JOIN_FIELD.number = 5
LOGINSM_JOIN_FIELD.index = 4
LOGINSM_JOIN_FIELD.label = 3
LOGINSM_JOIN_FIELD.has_default_value = false
LOGINSM_JOIN_FIELD.default_value = {}
LOGINSM_JOIN_FIELD.message_type = JULEBU
LOGINSM_JOIN_FIELD.type = 11
LOGINSM_JOIN_FIELD.cpp_type = 10

LOGINSM_MY_FIELD.name = "my"
LOGINSM_MY_FIELD.full_name = ".msg.LoginMessage.LoginSm.my"
LOGINSM_MY_FIELD.number = 6
LOGINSM_MY_FIELD.index = 5
LOGINSM_MY_FIELD.label = 3
LOGINSM_MY_FIELD.has_default_value = false
LOGINSM_MY_FIELD.default_value = {}
LOGINSM_MY_FIELD.message_type = JULEBU
LOGINSM_MY_FIELD.type = 11
LOGINSM_MY_FIELD.cpp_type = 10

LOGINSM_CREATEJ_FIELD.name = "createJ"
LOGINSM_CREATEJ_FIELD.full_name = ".msg.LoginMessage.LoginSm.createJ"
LOGINSM_CREATEJ_FIELD.number = 7
LOGINSM_CREATEJ_FIELD.index = 6
LOGINSM_CREATEJ_FIELD.label = 1
LOGINSM_CREATEJ_FIELD.has_default_value = false
LOGINSM_CREATEJ_FIELD.default_value = false
LOGINSM_CREATEJ_FIELD.type = 8
LOGINSM_CREATEJ_FIELD.cpp_type = 7

LOGINSM_NOTICE_FIELD.name = "notice"
LOGINSM_NOTICE_FIELD.full_name = ".msg.LoginMessage.LoginSm.notice"
LOGINSM_NOTICE_FIELD.number = 8
LOGINSM_NOTICE_FIELD.index = 7
LOGINSM_NOTICE_FIELD.label = 1
LOGINSM_NOTICE_FIELD.has_default_value = false
LOGINSM_NOTICE_FIELD.default_value = ""
LOGINSM_NOTICE_FIELD.type = 9
LOGINSM_NOTICE_FIELD.cpp_type = 9

LOGINSM.name = "LoginSm"
LOGINSM.full_name = ".msg.LoginMessage.LoginSm"
LOGINSM.nested_types = {}
LOGINSM.enum_types = {}
LOGINSM.fields = {LOGINSM_CODE_FIELD, LOGINSM_NICKNAME_FIELD, LOGINSM_HEADIMG_FIELD, LOGINSM_ROOMID_FIELD, LOGINSM_JOIN_FIELD, LOGINSM_MY_FIELD, LOGINSM_CREATEJ_FIELD, LOGINSM_NOTICE_FIELD}
LOGINSM.is_extendable = false
LOGINSM.extensions = {}
SWLOGINCM_UID_FIELD.name = "uid"
SWLOGINCM_UID_FIELD.full_name = ".msg.LoginMessage.SwLoginCm.uid"
SWLOGINCM_UID_FIELD.number = 1
SWLOGINCM_UID_FIELD.index = 0
SWLOGINCM_UID_FIELD.label = 1
SWLOGINCM_UID_FIELD.has_default_value = false
SWLOGINCM_UID_FIELD.default_value = 0
SWLOGINCM_UID_FIELD.type = 3
SWLOGINCM_UID_FIELD.cpp_type = 2

SWLOGINCM_SWCODE_FIELD.name = "swCode"
SWLOGINCM_SWCODE_FIELD.full_name = ".msg.LoginMessage.SwLoginCm.swCode"
SWLOGINCM_SWCODE_FIELD.number = 2
SWLOGINCM_SWCODE_FIELD.index = 1
SWLOGINCM_SWCODE_FIELD.label = 1
SWLOGINCM_SWCODE_FIELD.has_default_value = false
SWLOGINCM_SWCODE_FIELD.default_value = ""
SWLOGINCM_SWCODE_FIELD.type = 9
SWLOGINCM_SWCODE_FIELD.cpp_type = 9

SWLOGINCM_SID_FIELD.name = "sId"
SWLOGINCM_SID_FIELD.full_name = ".msg.LoginMessage.SwLoginCm.sId"
SWLOGINCM_SID_FIELD.number = 3
SWLOGINCM_SID_FIELD.index = 2
SWLOGINCM_SID_FIELD.label = 1
SWLOGINCM_SID_FIELD.has_default_value = false
SWLOGINCM_SID_FIELD.default_value = 0
SWLOGINCM_SID_FIELD.type = 5
SWLOGINCM_SID_FIELD.cpp_type = 1

SWLOGINCM_TYPE_FIELD.name = "type"
SWLOGINCM_TYPE_FIELD.full_name = ".msg.LoginMessage.SwLoginCm.type"
SWLOGINCM_TYPE_FIELD.number = 4
SWLOGINCM_TYPE_FIELD.index = 3
SWLOGINCM_TYPE_FIELD.label = 1
SWLOGINCM_TYPE_FIELD.has_default_value = false
SWLOGINCM_TYPE_FIELD.default_value = 0
SWLOGINCM_TYPE_FIELD.type = 5
SWLOGINCM_TYPE_FIELD.cpp_type = 1

SWLOGINCM.name = "SwLoginCm"
SWLOGINCM.full_name = ".msg.LoginMessage.SwLoginCm"
SWLOGINCM.nested_types = {}
SWLOGINCM.enum_types = {}
SWLOGINCM.fields = {SWLOGINCM_UID_FIELD, SWLOGINCM_SWCODE_FIELD, SWLOGINCM_SID_FIELD, SWLOGINCM_TYPE_FIELD}
SWLOGINCM.is_extendable = false
SWLOGINCM.extensions = {}
SWLOGINSM_CODE_FIELD.name = "code"
SWLOGINSM_CODE_FIELD.full_name = ".msg.LoginMessage.SwLoginSm.code"
SWLOGINSM_CODE_FIELD.number = 1
SWLOGINSM_CODE_FIELD.index = 0
SWLOGINSM_CODE_FIELD.label = 1
SWLOGINSM_CODE_FIELD.has_default_value = false
SWLOGINSM_CODE_FIELD.default_value = 0
SWLOGINSM_CODE_FIELD.type = 5
SWLOGINSM_CODE_FIELD.cpp_type = 1

SWLOGINSM_LOGINSM_FIELD.name = "loginSm"
SWLOGINSM_LOGINSM_FIELD.full_name = ".msg.LoginMessage.SwLoginSm.loginSm"
SWLOGINSM_LOGINSM_FIELD.number = 2
SWLOGINSM_LOGINSM_FIELD.index = 1
SWLOGINSM_LOGINSM_FIELD.label = 1
SWLOGINSM_LOGINSM_FIELD.has_default_value = false
SWLOGINSM_LOGINSM_FIELD.default_value = nil
SWLOGINSM_LOGINSM_FIELD.message_type = LOGINSM
SWLOGINSM_LOGINSM_FIELD.type = 11
SWLOGINSM_LOGINSM_FIELD.cpp_type = 10

SWLOGINSM.name = "SwLoginSm"
SWLOGINSM.full_name = ".msg.LoginMessage.SwLoginSm"
SWLOGINSM.nested_types = {}
SWLOGINSM.enum_types = {}
SWLOGINSM.fields = {SWLOGINSM_CODE_FIELD, SWLOGINSM_LOGINSM_FIELD}
SWLOGINSM.is_extendable = false
SWLOGINSM.extensions = {}
PLAYERCAST_DIAMOND_FIELD.name = "diamond"
PLAYERCAST_DIAMOND_FIELD.full_name = ".msg.LoginMessage.PlayerCast.diamond"
PLAYERCAST_DIAMOND_FIELD.number = 1
PLAYERCAST_DIAMOND_FIELD.index = 0
PLAYERCAST_DIAMOND_FIELD.label = 1
PLAYERCAST_DIAMOND_FIELD.has_default_value = false
PLAYERCAST_DIAMOND_FIELD.default_value = 0
PLAYERCAST_DIAMOND_FIELD.type = 5
PLAYERCAST_DIAMOND_FIELD.cpp_type = 1

PLAYERCAST.name = "PlayerCast"
PLAYERCAST.full_name = ".msg.LoginMessage.PlayerCast"
PLAYERCAST.nested_types = {}
PLAYERCAST.enum_types = {}
PLAYERCAST.fields = {PLAYERCAST_DIAMOND_FIELD}
PLAYERCAST.is_extendable = false
PLAYERCAST.extensions = {}
GAMERECORD_NICKNAME_FIELD.name = "nickname"
GAMERECORD_NICKNAME_FIELD.full_name = ".msg.LoginMessage.GameRecord.nickname"
GAMERECORD_NICKNAME_FIELD.number = 1
GAMERECORD_NICKNAME_FIELD.index = 0
GAMERECORD_NICKNAME_FIELD.label = 3
GAMERECORD_NICKNAME_FIELD.has_default_value = false
GAMERECORD_NICKNAME_FIELD.default_value = {}
GAMERECORD_NICKNAME_FIELD.type = 9
GAMERECORD_NICKNAME_FIELD.cpp_type = 9

GAMERECORD_COIN_FIELD.name = "coin"
GAMERECORD_COIN_FIELD.full_name = ".msg.LoginMessage.GameRecord.coin"
GAMERECORD_COIN_FIELD.number = 2
GAMERECORD_COIN_FIELD.index = 1
GAMERECORD_COIN_FIELD.label = 3
GAMERECORD_COIN_FIELD.has_default_value = false
GAMERECORD_COIN_FIELD.default_value = {}
GAMERECORD_COIN_FIELD.type = 5
GAMERECORD_COIN_FIELD.cpp_type = 1

GAMERECORD_ROUND_FIELD.name = "round"
GAMERECORD_ROUND_FIELD.full_name = ".msg.LoginMessage.GameRecord.round"
GAMERECORD_ROUND_FIELD.number = 3
GAMERECORD_ROUND_FIELD.index = 2
GAMERECORD_ROUND_FIELD.label = 1
GAMERECORD_ROUND_FIELD.has_default_value = false
GAMERECORD_ROUND_FIELD.default_value = 0
GAMERECORD_ROUND_FIELD.type = 5
GAMERECORD_ROUND_FIELD.cpp_type = 1

GAMERECORD_ROOMID_FIELD.name = "roomId"
GAMERECORD_ROOMID_FIELD.full_name = ".msg.LoginMessage.GameRecord.roomId"
GAMERECORD_ROOMID_FIELD.number = 4
GAMERECORD_ROOMID_FIELD.index = 3
GAMERECORD_ROOMID_FIELD.label = 1
GAMERECORD_ROOMID_FIELD.has_default_value = false
GAMERECORD_ROOMID_FIELD.default_value = ""
GAMERECORD_ROOMID_FIELD.type = 9
GAMERECORD_ROOMID_FIELD.cpp_type = 9

GAMERECORD_PLAYTYPE_FIELD.name = "playType"
GAMERECORD_PLAYTYPE_FIELD.full_name = ".msg.LoginMessage.GameRecord.playType"
GAMERECORD_PLAYTYPE_FIELD.number = 5
GAMERECORD_PLAYTYPE_FIELD.index = 4
GAMERECORD_PLAYTYPE_FIELD.label = 1
GAMERECORD_PLAYTYPE_FIELD.has_default_value = false
GAMERECORD_PLAYTYPE_FIELD.default_value = 0
GAMERECORD_PLAYTYPE_FIELD.type = 5
GAMERECORD_PLAYTYPE_FIELD.cpp_type = 1

GAMERECORD_TIME_FIELD.name = "time"
GAMERECORD_TIME_FIELD.full_name = ".msg.LoginMessage.GameRecord.time"
GAMERECORD_TIME_FIELD.number = 6
GAMERECORD_TIME_FIELD.index = 5
GAMERECORD_TIME_FIELD.label = 1
GAMERECORD_TIME_FIELD.has_default_value = false
GAMERECORD_TIME_FIELD.default_value = 0
GAMERECORD_TIME_FIELD.type = 3
GAMERECORD_TIME_FIELD.cpp_type = 2

GAMERECORD_INDEX_FIELD.name = "index"
GAMERECORD_INDEX_FIELD.full_name = ".msg.LoginMessage.GameRecord.index"
GAMERECORD_INDEX_FIELD.number = 7
GAMERECORD_INDEX_FIELD.index = 6
GAMERECORD_INDEX_FIELD.label = 1
GAMERECORD_INDEX_FIELD.has_default_value = false
GAMERECORD_INDEX_FIELD.default_value = 0
GAMERECORD_INDEX_FIELD.type = 5
GAMERECORD_INDEX_FIELD.cpp_type = 1

GAMERECORD_WINNER_FIELD.name = "winner"
GAMERECORD_WINNER_FIELD.full_name = ".msg.LoginMessage.GameRecord.winner"
GAMERECORD_WINNER_FIELD.number = 8
GAMERECORD_WINNER_FIELD.index = 7
GAMERECORD_WINNER_FIELD.label = 1
GAMERECORD_WINNER_FIELD.has_default_value = false
GAMERECORD_WINNER_FIELD.default_value = 0
GAMERECORD_WINNER_FIELD.type = 5
GAMERECORD_WINNER_FIELD.cpp_type = 1

GAMERECORD.name = "GameRecord"
GAMERECORD.full_name = ".msg.LoginMessage.GameRecord"
GAMERECORD.nested_types = {}
GAMERECORD.enum_types = {}
GAMERECORD.fields = {GAMERECORD_NICKNAME_FIELD, GAMERECORD_COIN_FIELD, GAMERECORD_ROUND_FIELD, GAMERECORD_ROOMID_FIELD, GAMERECORD_PLAYTYPE_FIELD, GAMERECORD_TIME_FIELD, GAMERECORD_INDEX_FIELD, GAMERECORD_WINNER_FIELD}
GAMERECORD.is_extendable = false
GAMERECORD.extensions = {}
ROUND_COIN_FIELD.name = "coin"
ROUND_COIN_FIELD.full_name = ".msg.LoginMessage.Round.coin"
ROUND_COIN_FIELD.number = 1
ROUND_COIN_FIELD.index = 0
ROUND_COIN_FIELD.label = 3
ROUND_COIN_FIELD.has_default_value = false
ROUND_COIN_FIELD.default_value = {}
ROUND_COIN_FIELD.type = 5
ROUND_COIN_FIELD.cpp_type = 1

ROUND_TIME_FIELD.name = "time"
ROUND_TIME_FIELD.full_name = ".msg.LoginMessage.Round.time"
ROUND_TIME_FIELD.number = 2
ROUND_TIME_FIELD.index = 1
ROUND_TIME_FIELD.label = 1
ROUND_TIME_FIELD.has_default_value = false
ROUND_TIME_FIELD.default_value = 0
ROUND_TIME_FIELD.type = 3
ROUND_TIME_FIELD.cpp_type = 2

ROUND_BANKERSEETINDEX_FIELD.name = "bankerSeetIndex"
ROUND_BANKERSEETINDEX_FIELD.full_name = ".msg.LoginMessage.Round.bankerSeetIndex"
ROUND_BANKERSEETINDEX_FIELD.number = 3
ROUND_BANKERSEETINDEX_FIELD.index = 2
ROUND_BANKERSEETINDEX_FIELD.label = 1
ROUND_BANKERSEETINDEX_FIELD.has_default_value = false
ROUND_BANKERSEETINDEX_FIELD.default_value = 0
ROUND_BANKERSEETINDEX_FIELD.type = 5
ROUND_BANKERSEETINDEX_FIELD.cpp_type = 1

ROUND_BATTLEBACKINDEX_FIELD.name = "battlebackIndex"
ROUND_BATTLEBACKINDEX_FIELD.full_name = ".msg.LoginMessage.Round.battlebackIndex"
ROUND_BATTLEBACKINDEX_FIELD.number = 4
ROUND_BATTLEBACKINDEX_FIELD.index = 3
ROUND_BATTLEBACKINDEX_FIELD.label = 1
ROUND_BATTLEBACKINDEX_FIELD.has_default_value = false
ROUND_BATTLEBACKINDEX_FIELD.default_value = 0
ROUND_BATTLEBACKINDEX_FIELD.type = 5
ROUND_BATTLEBACKINDEX_FIELD.cpp_type = 1

ROUND.name = "Round"
ROUND.full_name = ".msg.LoginMessage.Round"
ROUND.nested_types = {}
ROUND.enum_types = {}
ROUND.fields = {ROUND_COIN_FIELD, ROUND_TIME_FIELD, ROUND_BANKERSEETINDEX_FIELD, ROUND_BATTLEBACKINDEX_FIELD}
ROUND.is_extendable = false
ROUND.extensions = {}
GAMERECORDCM_INDEX_FIELD.name = "index"
GAMERECORDCM_INDEX_FIELD.full_name = ".msg.LoginMessage.GameRecordCm.index"
GAMERECORDCM_INDEX_FIELD.number = 1
GAMERECORDCM_INDEX_FIELD.index = 0
GAMERECORDCM_INDEX_FIELD.label = 1
GAMERECORDCM_INDEX_FIELD.has_default_value = false
GAMERECORDCM_INDEX_FIELD.default_value = 0
GAMERECORDCM_INDEX_FIELD.type = 5
GAMERECORDCM_INDEX_FIELD.cpp_type = 1

GAMERECORDCM.name = "GameRecordCm"
GAMERECORDCM.full_name = ".msg.LoginMessage.GameRecordCm"
GAMERECORDCM.nested_types = {}
GAMERECORDCM.enum_types = {}
GAMERECORDCM.fields = {GAMERECORDCM_INDEX_FIELD}
GAMERECORDCM.is_extendable = false
GAMERECORDCM.extensions = {}
GAMERECORDSM_CODE_FIELD.name = "code"
GAMERECORDSM_CODE_FIELD.full_name = ".msg.LoginMessage.GameRecordSm.code"
GAMERECORDSM_CODE_FIELD.number = 1
GAMERECORDSM_CODE_FIELD.index = 0
GAMERECORDSM_CODE_FIELD.label = 1
GAMERECORDSM_CODE_FIELD.has_default_value = false
GAMERECORDSM_CODE_FIELD.default_value = 0
GAMERECORDSM_CODE_FIELD.type = 5
GAMERECORDSM_CODE_FIELD.cpp_type = 1

GAMERECORDSM_ROUND_FIELD.name = "round"
GAMERECORDSM_ROUND_FIELD.full_name = ".msg.LoginMessage.GameRecordSm.round"
GAMERECORDSM_ROUND_FIELD.number = 2
GAMERECORDSM_ROUND_FIELD.index = 1
GAMERECORDSM_ROUND_FIELD.label = 3
GAMERECORDSM_ROUND_FIELD.has_default_value = false
GAMERECORDSM_ROUND_FIELD.default_value = {}
GAMERECORDSM_ROUND_FIELD.message_type = ROUND
GAMERECORDSM_ROUND_FIELD.type = 11
GAMERECORDSM_ROUND_FIELD.cpp_type = 10

GAMERECORDSM_INDEX_FIELD.name = "index"
GAMERECORDSM_INDEX_FIELD.full_name = ".msg.LoginMessage.GameRecordSm.index"
GAMERECORDSM_INDEX_FIELD.number = 3
GAMERECORDSM_INDEX_FIELD.index = 2
GAMERECORDSM_INDEX_FIELD.label = 1
GAMERECORDSM_INDEX_FIELD.has_default_value = false
GAMERECORDSM_INDEX_FIELD.default_value = 0
GAMERECORDSM_INDEX_FIELD.type = 5
GAMERECORDSM_INDEX_FIELD.cpp_type = 1

GAMERECORDSM.name = "GameRecordSm"
GAMERECORDSM.full_name = ".msg.LoginMessage.GameRecordSm"
GAMERECORDSM.nested_types = {}
GAMERECORDSM.enum_types = {}
GAMERECORDSM.fields = {GAMERECORDSM_CODE_FIELD, GAMERECORDSM_ROUND_FIELD, GAMERECORDSM_INDEX_FIELD}
GAMERECORDSM.is_extendable = false
GAMERECORDSM.extensions = {}
PLAYERRECORDCAST_RECORD_FIELD.name = "record"
PLAYERRECORDCAST_RECORD_FIELD.full_name = ".msg.LoginMessage.PlayerRecordCast.record"
PLAYERRECORDCAST_RECORD_FIELD.number = 2
PLAYERRECORDCAST_RECORD_FIELD.index = 0
PLAYERRECORDCAST_RECORD_FIELD.label = 3
PLAYERRECORDCAST_RECORD_FIELD.has_default_value = false
PLAYERRECORDCAST_RECORD_FIELD.default_value = {}
PLAYERRECORDCAST_RECORD_FIELD.message_type = GAMERECORD
PLAYERRECORDCAST_RECORD_FIELD.type = 11
PLAYERRECORDCAST_RECORD_FIELD.cpp_type = 10

PLAYERRECORDCAST.name = "PlayerRecordCast"
PLAYERRECORDCAST.full_name = ".msg.LoginMessage.PlayerRecordCast"
PLAYERRECORDCAST.nested_types = {}
PLAYERRECORDCAST.enum_types = {}
PLAYERRECORDCAST.fields = {PLAYERRECORDCAST_RECORD_FIELD}
PLAYERRECORDCAST.is_extendable = false
PLAYERRECORDCAST.extensions = {}
PROXYRECORDCAST_RECORD_FIELD.name = "record"
PROXYRECORDCAST_RECORD_FIELD.full_name = ".msg.LoginMessage.ProxyRecordCast.record"
PROXYRECORDCAST_RECORD_FIELD.number = 1
PROXYRECORDCAST_RECORD_FIELD.index = 0
PROXYRECORDCAST_RECORD_FIELD.label = 3
PROXYRECORDCAST_RECORD_FIELD.has_default_value = false
PROXYRECORDCAST_RECORD_FIELD.default_value = {}
PROXYRECORDCAST_RECORD_FIELD.message_type = GAMERECORD
PROXYRECORDCAST_RECORD_FIELD.type = 11
PROXYRECORDCAST_RECORD_FIELD.cpp_type = 10

PROXYRECORDCAST.name = "ProxyRecordCast"
PROXYRECORDCAST.full_name = ".msg.LoginMessage.ProxyRecordCast"
PROXYRECORDCAST.nested_types = {}
PROXYRECORDCAST.enum_types = {}
PROXYRECORDCAST.fields = {PROXYRECORDCAST_RECORD_FIELD}
PROXYRECORDCAST.is_extendable = false
PROXYRECORDCAST.extensions = {}
CHARGECAST_DIAMOND_FIELD.name = "diamond"
CHARGECAST_DIAMOND_FIELD.full_name = ".msg.LoginMessage.ChargeCast.diamond"
CHARGECAST_DIAMOND_FIELD.number = 1
CHARGECAST_DIAMOND_FIELD.index = 0
CHARGECAST_DIAMOND_FIELD.label = 1
CHARGECAST_DIAMOND_FIELD.has_default_value = false
CHARGECAST_DIAMOND_FIELD.default_value = 0
CHARGECAST_DIAMOND_FIELD.type = 5
CHARGECAST_DIAMOND_FIELD.cpp_type = 1

CHARGECAST.name = "ChargeCast"
CHARGECAST.full_name = ".msg.LoginMessage.ChargeCast"
CHARGECAST.nested_types = {}
CHARGECAST.enum_types = {}
CHARGECAST.fields = {CHARGECAST_DIAMOND_FIELD}
CHARGECAST.is_extendable = false
CHARGECAST.extensions = {}
INVITECODECM_INDEX_FIELD.name = "index"
INVITECODECM_INDEX_FIELD.full_name = ".msg.LoginMessage.InviteCodeCm.index"
INVITECODECM_INDEX_FIELD.number = 1
INVITECODECM_INDEX_FIELD.index = 0
INVITECODECM_INDEX_FIELD.label = 1
INVITECODECM_INDEX_FIELD.has_default_value = false
INVITECODECM_INDEX_FIELD.default_value = ""
INVITECODECM_INDEX_FIELD.type = 9
INVITECODECM_INDEX_FIELD.cpp_type = 9

INVITECODECM.name = "InviteCodeCm"
INVITECODECM.full_name = ".msg.LoginMessage.InviteCodeCm"
INVITECODECM.nested_types = {}
INVITECODECM.enum_types = {}
INVITECODECM.fields = {INVITECODECM_INDEX_FIELD}
INVITECODECM.is_extendable = false
INVITECODECM.extensions = {}
INVITECODESM_CODE_FIELD.name = "code"
INVITECODESM_CODE_FIELD.full_name = ".msg.LoginMessage.InviteCodeSm.code"
INVITECODESM_CODE_FIELD.number = 1
INVITECODESM_CODE_FIELD.index = 0
INVITECODESM_CODE_FIELD.label = 1
INVITECODESM_CODE_FIELD.has_default_value = false
INVITECODESM_CODE_FIELD.default_value = 0
INVITECODESM_CODE_FIELD.type = 5
INVITECODESM_CODE_FIELD.cpp_type = 1

INVITECODESM_ROUND_FIELD.name = "round"
INVITECODESM_ROUND_FIELD.full_name = ".msg.LoginMessage.InviteCodeSm.round"
INVITECODESM_ROUND_FIELD.number = 2
INVITECODESM_ROUND_FIELD.index = 1
INVITECODESM_ROUND_FIELD.label = 3
INVITECODESM_ROUND_FIELD.has_default_value = false
INVITECODESM_ROUND_FIELD.default_value = {}
INVITECODESM_ROUND_FIELD.message_type = ROUND
INVITECODESM_ROUND_FIELD.type = 11
INVITECODESM_ROUND_FIELD.cpp_type = 10

INVITECODESM_INDEX_FIELD.name = "index"
INVITECODESM_INDEX_FIELD.full_name = ".msg.LoginMessage.InviteCodeSm.index"
INVITECODESM_INDEX_FIELD.number = 3
INVITECODESM_INDEX_FIELD.index = 2
INVITECODESM_INDEX_FIELD.label = 1
INVITECODESM_INDEX_FIELD.has_default_value = false
INVITECODESM_INDEX_FIELD.default_value = 0
INVITECODESM_INDEX_FIELD.type = 5
INVITECODESM_INDEX_FIELD.cpp_type = 1

INVITECODESM.name = "InviteCodeSm"
INVITECODESM.full_name = ".msg.LoginMessage.InviteCodeSm"
INVITECODESM.nested_types = {}
INVITECODESM.enum_types = {}
INVITECODESM.fields = {INVITECODESM_CODE_FIELD, INVITECODESM_ROUND_FIELD, INVITECODESM_INDEX_FIELD}
INVITECODESM.is_extendable = false
INVITECODESM.extensions = {}

ChargeCast = protobuf.Message(CHARGECAST)
GameRecord = protobuf.Message(GAMERECORD)
GameRecordCm = protobuf.Message(GAMERECORDCM)
GameRecordSm = protobuf.Message(GAMERECORDSM)
InviteCodeCm = protobuf.Message(INVITECODECM)
InviteCodeSm = protobuf.Message(INVITECODESM)
Julebu = protobuf.Message(JULEBU)
LoginCm = protobuf.Message(LOGINCM)
LoginSm = protobuf.Message(LOGINSM)
PlayerCast = protobuf.Message(PLAYERCAST)
PlayerRecordCast = protobuf.Message(PLAYERRECORDCAST)
ProxyRecordCast = protobuf.Message(PROXYRECORDCAST)
Round = protobuf.Message(ROUND)
SwLoginCm = protobuf.Message(SWLOGINCM)
SwLoginSm = protobuf.Message(SWLOGINSM)

