--Behaviour事件
module("BehaviourEvent",package.seeall)

BehaviourEvent = {}

--绑定gameObject的MonoBehaviour事件
--Start
--OnDestroy
--OnEnable
--OnDisable
--OnApplicationFocus
--OnApplicationPause
function BehaviourEvent:Bind(eventName,gameObject,fun)
    EventHelper.BindEvent(eventName,gameObject,fun)
end






