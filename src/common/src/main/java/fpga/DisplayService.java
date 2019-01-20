package fpga;

import util.Color;
import util.ImageLoader;
import util.Thread;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DisplayService extends Thread {
    /**
     * Frame list
     */
    private List<BufferedImage> frames_ = null;

    /**
     * Duration
     */
    private long duration_;

    /**
     * Brightness
     */
    private float brightness_;

    /**
     * Set transmitter
     */
    private Transmitter transmitter_ = null;

    /**
     * Color
     */
    private Color color_ = null;

    /**
     * Ready
     */
    private AtomicBoolean stop_ = new AtomicBoolean(false);

    /**
     * Image loader
     */
    private ImageLoader loader_ = null;

    /**
     * Constructor
     * @param duration duration
     * @param brightness float
     */
    public DisplayService(Transmitter transmitter, long duration, float brightness) {
        transmitter_ = transmitter;
        frames_ = new ArrayList<>();
        duration_ = duration;
        brightness_ = brightness;
        loader_ = new ImageLoader();
    }

    /**
     * Stop display
     */
    public void StopDisplay() {
        stop_.set(true);
    }

    /**
     * Set color
     * @param r red
     * @param g green
     * @param b blue
     */
    public void SetColor(int r, int g, int b) {
        color_ = new Color(r,g,b);
    }

    /**
     * Add frame
     * @param i buffer
     * @param process if pictures should be processed
     */
    public void AddFrame(BufferedImage i, boolean process) {
        // Process
        if (process) {
            frames_.add(loader_.ProcessImage(i, transmitter_.GetWidth(), transmitter_.GetHeight()));
        } else {
            frames_.add(i);
        }
    }

    /**
     * Set frames
     * @param frames as buffer
     */
    public void SetFramesFromBuffer(List<byte[]> frames, boolean process) {
        for (byte[] buffer : frames) {
            final BufferedImage t = loader_.FromBuffer(buffer);

            // If image is invalid | TODO: Log
            if (t == null)
                continue;

            // Add frame
            AddFrame(t, process);
        }
    }

    /**
     * Set frames
     * @param frames as ImageBuffer
     */
    public void SetFrames(List<BufferedImage> frames, boolean process) {
        for (BufferedImage t : frames) {
            // If image is invalid | TODO: Log
            if (t == null)
                continue;

            // Add frame
            AddFrame(t, process);
        }
    }

    /**
     * Run
     */
    public void run() {
        // Add seconds
        LocalTime localTime_ = LocalTime.now().plusSeconds(duration_);

        // If we are done
        while(LocalTime.now().isAfter(localTime_) || duration_ == 0) {
            try {
                // If we show frames
                if (frames_.size() >= 0) {
                    for (BufferedImage i : frames_) {
                        transmitter_.TransmitImage(i, brightness_);
                    }
                } else if (color_ != null) {
                    transmitter_.TransmitColor(color_, brightness_);
                }
            } catch (IOException e) {
                // TODO: Log
            }

            // If we have to stop
            if (stop_.get())
                break;
        }

        // Clear matrix
        Color c = new Color(0,0,0);
        try {
            transmitter_.TransmitColor(c, 0.0f);
        } catch (IOException e) {
            // TODO: Log (failed to clear)
        }
    }
}