package utils;

import java.io.Serializable;

public final class Vector2D implements Serializable {
	public double x,y;
	public Vector2D(){
		this.x=0;
		this.y=0;
	}
	public Vector2D(double x, double y){
		this.x=x;
		this.y=y;
	}
	public Vector2D(Vector2D vect){
		this.x = vect.x;
		this.y = vect.y;
	}
	public void set(double x, double y){
		this.x = x;
		this.y = y;
	}
	public void set(Vector2D v){
		this.x = 0+v.x;
		this.y =0+ v.y;
	}
	public boolean equals(Object o){
		Vector2D v = (Vector2D) o;
		return ( Math.abs(this.x - v.x) == 0 && Math.abs(this.y - v.y) == 0 );
	}
	public double mag(){
		return Math.hypot(this.x ,this.y);
	}
	public double theta(){
		return Math.atan2(this.y, this.x);
	}
	public String toString(){
		return "x = " + this.x + " y = " +this.y;
	}
	public void add(Vector2D v){
		this.x += v.x;
		this.y += v.y;
	}
	public void add(double x, double y){
		this.x += x;
		this.y += y;
	}
	public void add(Vector2D v, double fact){
		this.x += fact * v.x;
		this.y += fact * v.y;
	}
	public void subtract(Vector2D v){
		this.x += v.x * - 1;
		this.y += v.y * - 1;
	}
	public void subtract(double x, double y){
		this.x -= x;
		this.y -= y;
	}
	public void multiply(double fact){
		this.x *= fact;
		this.y *= fact;
	}
	public void wrap(double w, double h){
		this.x = (this.x +w) % w;
		this.y = (this.y + h) % h;
	}
	public void rotate(double theta){
		double xp = this.x;
		this.x = xp * Math.cos(theta) - this.y * Math.sin(theta);
		this.y = xp * Math.sin(theta) + this.y * Math.cos(theta);
	}
	public void normalise(){
		double magn = mag();
		if(magn > 0){
			this.x /= magn;
			this.y /= magn;
		}
	}
	public double scalarProduct(Vector2D v){
		return this.x * v.x + this.y * v.y;
	}
	public double dist(Vector2D v){
		Vector2D vc = new Vector2D(this);
		vc.subtract(v);
		return(vc.mag());
	}
    public Vector2D toWrapped(Vector2D v, double w, double h){
        double dx = v.x -this.x;
        if (Math.abs(dx) > w/2) dx = dx - Math.signum(dx) * w;
        double dy = v.y - this.y;
        if (Math.abs(dy) > h/2) dy = dy - Math.signum(dy) * h;
        return new Vector2D (dx,dy);
    }
    // distance of vectors in a wrapped world
    public double distWrapped(Vector2D v, double w, double h){
        return (this.toWrapped(v,w,h)).mag();
    }
    public Vector2D to(Vector2D v) {
        return new Vector2D(v.x-x, v.y-y); }
}
