--


local function localFn()
    print("local function +1")
    return 1
end


-- return min / max
function min_max(ismin, v1, v2)
    local v = localFn()
    if ismin then
        return math.min(v1, v2) + v
    else
        return math.max(v1, v2) + v
    end
end

