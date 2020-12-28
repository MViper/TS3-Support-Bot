package de.ts3tut.flo.main;

public class ChannelHistory {
	int channels = 0;
	
	public ChannelHistory() {
		
	}

	public void addChannel() {
		System.out.println(channels);
		channels++;
	}
	
	public void removeChannel() {
		if(channels > 0)
		channels--;
	}
	
	public int isChannelhopping() {
		if(channels == 8) {
			return 2;
		}if(channels >= 10) {
			return 1;
		}else {
			return 0;
		}
	}
}
