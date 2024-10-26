package me.t3sl4.upcortex.Model.Subscription;

public class Subscription {
    private String id;
    private String name;
    private String price;
    private int duration;
    private int delayDuration;
    private String durationType;
    private String content;
    private String scope;
    private String isActive;
    private String code;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getDelayDuration() { return delayDuration; }
    public void setDelayDuration(int delayDuration) { this.delayDuration = delayDuration; }

    public String getDurationType() { return durationType; }
    public void setDurationType(String durationType) { this.durationType = durationType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public String getIsActive() { return isActive; }
    public void setIsActive(String isActive) { this.isActive = isActive; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}