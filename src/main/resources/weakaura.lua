local keyIdToAbbrevMap = {
    [375] = "MISTS",
    [376] = "NW",
    [377] = "DOS",
    [378] = "HOA",
    [379] = "PF",
    [380] = "SD",
    [381] = "SOA",
    [382] = "TOP",
    [391] = "STRT",
    [392] = "GMBT",
}

local function isSavedInstancesLoaded()
    return IsAddOnLoaded("SavedInstances")
end

local function getKeyObjectForItemLink(itemLink)
    local _, itemId, mapId, mapLevel = strsplit(':', itemLink)
    local abbrev = keyIdToAbbrevMap[tonumber(mapId)]
    return {
        itemLink = itemLink,
        itemId = itemId,
        mapId = mapId,
        mapLevel = mapLevel,
        mapAbbrev = abbrev
    }
end

local function getCommandForItemLink(itemLink, charName)
    local keyObject = getKeyObjectForItemLink(itemLink)
    local keystoneCommand = "!keystone " .. charName:gsub(" ", "") .. " " .. keyObject.mapAbbrev .. " " .. keyObject.mapLevel
    return keystoneCommand
end

local function getKeyFromBags()
    for bagId = 0, 4 do
        for invId = 1, GetContainerNumSlots(bagId) do
            local itemId = GetContainerItemID(bagId, invId)
            if itemId and itemId == 180653 then
                local itemLink = GetContainerItemLink(bagId, invId)
                return itemLink
            end
        end
    end

    return nil
end

local function ProcessKey(itemLink, targetTable)
    local SI = _G.SavedInstances[1]
    local _, _, mapID, mapLevel = strsplit(':', itemLink)
    mapID = tonumber(mapID)
    mapLevel = tonumber(mapLevel)

    targetTable.link = itemLink
    targetTable.mapID = mapID
    targetTable.level = mapLevel
    targetTable.name = C_ChallengeMode.GetMapUIInfo(mapID)
    targetTable.color = "ffffffff"
    targetTable.ResetTime = SI.GetNextWeeklyResetTime()
end

local function refreshMythicKeyInfoSI()
    local SI = _G.SavedInstances[1]
    local t = SI.db.Toons[SI.thisToon]
    t.MythicKey = wipe(t.MythicKey or {})
    t.TimewornMythicKey = wipe(t.TimewornMythicKey or {})
    for bagID = 0, 4 do
        for invID = 1, GetContainerNumSlots(bagID) do
            local itemID = GetContainerItemID(bagID, invID)
            if itemID and itemID == 180653 then
                ProcessKey(GetContainerItemLink(bagID, invID), t.MythicKey)
            elseif itemID and itemID == 187786 then
                ProcessKey(GetContainerItemLink(bagID, invID), t.TimewornMythicKey)
            end
        end
    end
    SI.db.Toons[SI.thisToon] = t
end

local function getKeystoneCommandsFromSI()
    local SI = _G.SavedInstances[1]
    local toons = SI.db.Toons

    refreshMythicKeyInfoSI()

    local commands = ""

    for charName, toon in pairs(toons) do
        local keyLink = toon.MythicKey.link
        if keyLink then
            commands = commands .. getCommandForItemLink(keyLink, charName) .. "\n"
        end
    end

    return commands
end

local function getKeystoneCommand()
    if (isSavedInstancesLoaded()) then
        return getKeystoneCommandsFromSI()
    else
        local itemLink = getKeyFromBags()
        local name = UnitName("player")
        local realm = GetRealmName()
        local charName = name .. "-" .. realm
        return getCommandForItemLink(itemLink, charName)
    end
end

local function showFrame(text, manualShow)
    if not _G.BeltipMythicPlusHelperFrame then
        -- Main Frame
        local f = CreateFrame("Frame", "BeltipMythicPlusHelperFrame", UIParent, "DialogBoxFrame")
        -- load position from local DB
        f:SetPoint("RIGHT")
        f:SetSize(400, 200)
        f:SetBackdrop({
            bgFile = "Interface\\DialogFrame\\UI-DialogBox-Background",
            edgeFile = "Interface\\PVPFrame\\UI-Character-PVP-Highlight",
            edgeSize = 16,
            insets = { left = 8, right = 8, top = 8, bottom = 8 },
        })
        f:SetMovable(true)
        f:SetClampedToScreen(true)
        f:SetScript("OnMouseDown", function(self, button)
            if button == "LeftButton" then
                self:StartMoving()
            end
        end)
        f:SetScript("OnMouseUp", function(self, button)
            self:StopMovingOrSizing()
        end)

        -- scroll frame
        local sf = CreateFrame("ScrollFrame", "BeltipMythicPlusHelperScrollFrame", f, "UIPanelScrollFrameTemplate")
        sf:SetPoint("LEFT", 16, 0)
        sf:SetPoint("RIGHT", -32, 0)
        sf:SetPoint("TOP", 0, -32)
        sf:SetPoint("BOTTOM", BeltipMythicPlusHelperFrameButton, "TOP", 0, 0)

        -- edit box
        local eb = CreateFrame("EditBox", "BeltipMythicPlusHelperEditBox", BeltipMythicPlusHelperScrollFrame)
        eb:SetSize(sf:GetSize())
        eb:SetMultiLine(true)
        eb:SetAutoFocus(true)
        eb:SetFontObject("ChatFontNormal")
        eb:SetScript("OnEscapePressed", function()
            f:Hide()
        end)
        sf:SetScrollChild(eb)

        -- resizing
        f:SetResizable(true)
        f:SetMinResize(150, 100)
        local rb = CreateFrame("Button", "BeltipMythicPlusHelperResizeButton", f)
        rb:SetPoint("BOTTOMRIGHT", -6, 7)
        rb:SetSize(16, 16)

        rb:SetNormalTexture("Interface\\ChatFrame\\UI-ChatIM-SizeGrabber-Up")
        rb:SetHighlightTexture("Interface\\ChatFrame\\UI-ChatIM-SizeGrabber-Highlight")
        rb:SetPushedTexture("Interface\\ChatFrame\\UI-ChatIM-SizeGrabber-Down")

        rb:SetScript("OnMouseDown", function(self, button)
            if button == "LeftButton" then
                f:StartSizing("BOTTOMRIGHT")
                self:GetHighlightTexture():Hide() -- more noticeable
            end
        end)
        rb:SetScript("OnMouseUp", function(self, button)
            f:StopMovingOrSizing()
            self:GetHighlightTexture():Show()
            eb:SetWidth(sf:GetWidth())
        end)

        _G.BeltipMythicPlusHelperFrame = f
    end
    BeltipMythicPlusHelperEditBox:SetText(text)
    if manualShow then
        BeltipMythicPlusHelperEditBox:HighlightText()
    end
    return _G.BeltipMythicPlusHelperFrame
end

local LDB = LibStub("LibDataBroker-1.1")
local LDBIcon = LibStub("LibDBIcon-1.0")

aura_env.recheckIcon = function()
    LDBIcon:IconCallback(nil, "BeltipMythicPlusHelperDBI", "icon", 460856)
end
local recheckIcon = aura_env.recheckIcon

local function createTooltip(tooltip)
    tooltip:ClearLines()
    tooltip:AddLine("Beltip Mythic Plus Helper")
end

aura_env.archive = aura_env.archive or WeakAuras.LoadFromArchive("RawData", aura_env.id .. " SavedData")
aura_env.LDBDisplay = aura_env.LDBDisplay or LDB:NewDataObject("BeltipMythicPlusHelperDBI", {
    type = "launcher",
    text = "Open Beltip Mythic Keystone Helper",
    icon = 460856,
    OnClick = function()
        local f = showFrame(getKeystoneCommand(), true)
        f:Show()
        recheckIcon()
    end,
    OnTooltipShow = function(tooltip)
        createTooltip(tooltip)
        recheckIcon()
    end,
})

local registered = LDBIcon:IsRegistered("BeltipMythicPlusHelperDBI")

if aura_env.LDBDisplay and
        not aura_env.config.headless and
        not registered then
    LDBIcon:Register("BeltipMythicPlusHelperDBI", aura_env.LDBDisplay, aura_env.archive)
end

if registered then
    if aura_env.config.headless then
        LDBIcon:Hide("BeltipMythicPlusHelperDBI")
    else
        LDBIcon:Show("BeltipMythicPlusHelperDBI")
    end
end

aura_env.showFrame = function()
    local f = showFrame(getKeystoneCommand(), false)
    f:Show()
end

