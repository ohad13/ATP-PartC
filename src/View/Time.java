package View;


public class Time {
    private int milisecond;
    private int minute;
    private int second;

    public Time(int minute, int second,int milisecond) {

        this.minute = minute;
        this.second = second;
        this.milisecond = milisecond;
    }

    public Time(String currentTime) {
        String[] time = currentTime.split(":");
        minute = Integer.parseInt(time[0]);
        second = Integer.parseInt(time[1]);
        milisecond = Integer.parseInt(time[2]);

    }

    public String getCurrentTime(){
        return   + minute + ":" + second + ":" +  milisecond;
    }

    public void oneSecondPassed(){
        milisecond++;
        if(milisecond == 100){
            second++;
            milisecond = 0;
            if(second == 60){
                minute++;
                minute = 0;
            }
        }
    }
}