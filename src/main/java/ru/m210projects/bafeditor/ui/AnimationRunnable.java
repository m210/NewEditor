package ru.m210projects.bafeditor.ui;

public abstract class AnimationRunnable implements Runnable {

	private boolean running = true;
	private boolean paused = false;
	private int timer = 1000;
	private int clock = 0;

	public abstract void process(int clock);

	@Override
	public void run() {
		while (running) {
			if (paused) {
				try {
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException ex) {
					break;
				}
			}
			process(clock++);

			try {
				Thread.sleep(timer);
			} catch (InterruptedException e) {
			}
		}
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public void dispose() {
		running = false;
		resume();
	}

	public void pause() {
		paused = true;
		clock = 0;
	}

	public boolean isRunning() {
		return !paused;
	}

	public synchronized void resume() {
		paused = false;
		notify();
	}
}
