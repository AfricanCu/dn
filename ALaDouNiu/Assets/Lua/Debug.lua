Debug = {}

Debug.debugmode = false

function Debug.log(info)
    if Debug.debugmode then
        print(info)
    end
end