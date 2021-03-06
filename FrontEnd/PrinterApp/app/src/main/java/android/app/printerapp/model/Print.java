package android.app.printerapp.model;

/**
 * A model for a print
 */

public class Print implements DataEntry{
    private String id;
    private String buildsId;
    private String operatorId;
    private String machineId;
    private String powderWeightStart;
    private String powderWeightEnd;
    private String buildPlatformMaterial;
    private String buildPlatformWeight;
    private String endTime;
    private String startTime;

    @Override
    public String getName() {
        return "P" + id;
    }

    public String getId(){
        return id;
    }

    @Override
    public String getCreationDate() {
        return startTime;
    }

    @Override
    public String getIdName() {
        return "P" + id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getBuildsId(){
        return buildsId;
    }

    public void setBuildsId(String id){
        this.buildsId = id;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPowderWeightStart(){
        return powderWeightStart;
    }

    public void setPowderWeightStart(String powderWeightStart){
        this.powderWeightStart = powderWeightStart;
    }

    public String getPowderWeightEnd(){
        return powderWeightEnd;
    }

    public void setPowderWeightEnd(String powderWeightEnd){
        this.powderWeightEnd = powderWeightEnd;
    }

    public String getBuildPlatformWeight(){
        return buildPlatformWeight;
    }

    public void setBuildPlatformWeight(String buildPlatformWeight){
        this.buildPlatformWeight= buildPlatformWeight;
    }

    public String getBuildPlatformMaterial(){
        return buildPlatformMaterial;
    }

    public void setBuildPlatformMaterial(String buildPlatformMaterial){
        this.buildPlatformMaterial = buildPlatformMaterial;
    }

    public String getEndTime(){
        return endTime;
    }

    public void setEndTimel(String endTime){
        this.endTime= endTime;
    }

    public String getStartTime(){
        return startTime;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return id + ", " + buildsId + ", " + operatorId + ", " + machineId + ", " +
                powderWeightStart + ", " + powderWeightEnd + ", " + buildPlatformMaterial
                + ", " + buildPlatformWeight + ", " + startTime + ", " + endTime;
    }
}
