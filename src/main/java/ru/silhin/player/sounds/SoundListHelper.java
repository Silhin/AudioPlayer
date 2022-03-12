package ru.silhin.player.sounds;

import javafx.scene.media.Media;
import ru.silhin.player.events.SoundMetadataEvent;
import ru.silhin.player.logger.SoundLogManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

public final class SoundListHelper {
    private static final Logger LOGGER = SoundLogManager.getLogger();

    private volatile static SoundListHelper INSTANCE;

    public synchronized static void create() {
        LOGGER.info("SoundListHelper created by " + Thread.currentThread().getName());
        INSTANCE = new SoundListHelper(new ArrayList<>());
    }

    public static SoundListHelper getInstance() {
        return INSTANCE;
    }

    /**
     * The offset operation is performed by the <b>native</b> method, which is faster,
     * so ArrayList will insert the element faster.
     */
    private final ArrayList<Media> playlist;

    private SoundListHelper(ArrayList<Media> playlist) {
        this.playlist = playlist;
    }

    public void addMediaOnPlaylist(Media media) {
        this.playlist.add(media);
    }

    public ArrayList<Media> getPlaylist() {
        return playlist;
    }

    public Collection<SoundView> getSoundPlaylist() {
        ArrayList<SoundView> soundPlaylists = new ArrayList<>();
        Iterator<Media> iterator = this.getPlaylist().iterator();
        short i = 0;
        while (iterator.hasNext()) {
            SoundMetadataEvent media = new SoundMetadataEvent(iterator.next());
            soundPlaylists.add(new SoundView(
                    i,
                    media.getImage(),
                    media.getTitle()
            ));
            ++i;
        }
        return soundPlaylists;
    }
}
