package View;

public class Time {
    private int millisecond;
    private int minute;
    private int second;

    // constructor
    public Time(int minute, int second, int millisecond) {
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
    }

    // copy constructor
    public Time(Time time) {
        this.minute = time.minute;
        this.second = time.second;
        this.millisecond = time.millisecond;
    }

    // constructor by string
    public Time(String currentTime) {
        String[] time = currentTime.split(":");
        minute = Integer.parseInt(time[0]);
        second = Integer.parseInt(time[1]);
        millisecond = Integer.parseInt(time[2]);
    }

    // check if the time we got is bigger then the one we received to the function
    boolean isGreaterThen(Time b) { // return true if what i got is bigger.
        int myTime = this.getTime();
        int bTime = b.getTime();
        return bTime > myTime;
    }

    // return the current time.
    public int getTime() {
        return this.millisecond + this.second * 60 + this.minute * 60 * 60;
    }

    // set a new time.
    public void setTime(int minute, int second, int millisecond) {
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
    }

    // return string of the current time.
    public String getCurrentTime() {
        String min, sec;
        if (minute < 10)
            min = "0" + minute;
        else min = String.valueOf(minute);
        if (second < 10)
            sec = "0" + second;
        else
            sec = String.valueOf(second);
        return min + ":" + sec + ":" + millisecond;
    }

    public void oneSecondPassed() {
        millisecond++;
        if (millisecond == 100) {
            second++;
            millisecond = 0;
            if (second == 60) {
                minute++;
                second = 0;
            }
        }
    }
}