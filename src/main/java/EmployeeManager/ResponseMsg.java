package EmployeeManager;

public class ResponseMsg {
    private String num;
    private String msg;

    public ResponseMsg() {

    }

    public ResponseMsg(String num, String msg) {
        this.num = num;
        this.msg = msg;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
