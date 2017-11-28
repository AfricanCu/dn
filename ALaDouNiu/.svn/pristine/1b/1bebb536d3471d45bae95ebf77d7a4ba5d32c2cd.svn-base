module("MyMath", package.seeall)

function MyMath.GetIntPart(x)
    if x <= 0 then
        return math.ceil(x)
    end
    if math.ceil(x) == x then
        x = math.ceil(x)
    else
        x = math.ceil(x)-1
    end

    return x
end

function MyMath.rad(d)
    return d * math.pi / 180
end

--返回距离为米
function MyMath.LantitudeLongitudeDist(loc1, loc2)
    if loc1==nil or loc2==nil then
        return 999999
    end
    local EARTH_RADIUS = 6378137 --赤道半径(单位m) 
    local radLat1 = MyMath.rad(loc1.w);  
    local radLat2 = MyMath.rad(loc2.w);  
    local radLon1 = MyMath.rad(loc1.j);  
    local radLon2 = MyMath.rad(loc2.j);  

    if radLat1 < 0 then 
        radLat1 = math.pi / 2 + math.abs(radLat1) -- south  
    end
    if radLat1 > 0 then
        radLat1 = math.pi / 2 - math.abs(radLat1) --north  
    end

    if radLon1 < 0 then
        radLon1 = math.pi * 2 - math.abs(radLon1) --west
    end

    if radLat2 < 0 then 
        radLat2 = math.pi / 2 + math.abs(radLat2) --south
    end  
    if radLat2 > 0 then
        radLat2 = math.pi / 2 - math.abs(radLat2) --north
    end  

    if radLon2 < 0 then
        radLon2 = math.pi * 2 - math.abs(radLon2) --west
    end

    local x1 = EARTH_RADIUS * math.cos(radLon1) * math.sin(radLat1)
    local y1 = EARTH_RADIUS * math.sin(radLon1) * math.sin(radLat1)
    local z1 = EARTH_RADIUS * math.cos(radLat1)

    local x2 = EARTH_RADIUS * math.cos(radLon2) * math.sin(radLat2)
    local y2 = EARTH_RADIUS * math.sin(radLon2) * math.sin(radLat2)
    local z2 = EARTH_RADIUS * math.cos(radLat2)

    local d = math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2))
    --余弦定理求夹角  
    local theta = math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS))
    local dist = theta * EARTH_RADIUS;  
    return dist
end