package com.tugu.ilkproje;

import java.util.ArrayList;
import java.util.List;

public class Day {
	public List<Lecture> lectures;
	public Day()
	{
		lectures = new ArrayList<Lecture>();
	}
	public void addLecture(Lecture l) {
		lectures.add(l);
	}
}
