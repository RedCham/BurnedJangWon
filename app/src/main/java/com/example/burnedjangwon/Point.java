package com.example.burnedjangwon;

public class Point implements Comparable<Point> {
    private String id;
    private String point;

    public String getID() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public  int getPointInt() {
        return Integer.parseInt(point);
    }

    @Override
    public int compareTo(Point p) {
        return this.point.compareTo(p.point);
    }
}
