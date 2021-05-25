package View;


public class Time {
    private int milisecond;
    private int minute;
    private int second;

    public Time(int minute, int second, int milisecond) {

        this.minute = minute;
        this.second = second;
        this.milisecond = milisecond;
    }

    public Time(Time time) {
        this.minute = time.minute;
        this.second = time.second;
        this.milisecond = time.milisecond;

    }

    public Time(String currentTime) {
        String[] time = currentTime.split(":");
        minute = Integer.parseInt(time[0]);
        second = Integer.parseInt(time[1]);
        milisecond = Integer.parseInt(time[2]);

    }

    boolean isgreaterThen(Time b) { // return ture if what i got is bigger
        int mytime = this.getTime();
        int btime = b.getTime();
        if (btime > mytime)
            return true;
        return false;
    }

    public int getTime() {
        int t = this.milisecond + this.second * 60 + this.minute * 60 * 60;
        return t;
    }

    public void setTime(int minute, int second, int milisecond) {
        this.minute = minute;
        this.second = second;
        this.milisecond = milisecond;
    }

    public String getCurrentTime() {
        String min, sec;
        if (minute < 10)
            min = "0" + minute;
        else min = String.valueOf(minute);
        if (second < 10)
            sec = "0" + second;
        else
            sec= String.valueOf(second);
        return min + ":" + sec + ":" + milisecond;
    }

    public void oneSecondPassed() {
        milisecond++;
        if (milisecond == 100) {
            second++;
            milisecond = 0;
            if (second == 60) {
                minute++;
                second = 0;
            }
        }
    }
}