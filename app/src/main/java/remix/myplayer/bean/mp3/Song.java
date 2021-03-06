package remix.myplayer.bean.mp3;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import java.io.File;
import remix.myplayer.App;
import remix.myplayer.util.SPUtil;
import timber.log.Timber;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Remix on 2015/11/30.
 */

/**
 * 歌曲信息
 */
public class Song implements Cloneable, Parcelable {

  public static Song EMPTY_SONG = new Song(-1, "", "", "", -1, "", -1, -1, "", "", -1, "", "", -1);
  /**
   * 所有列表是否显示文件名
   */
  public static boolean SHOW_DISPLAYNAME = SPUtil
      .getValue(App.getContext(), SPUtil.SETTING_KEY.NAME, SPUtil.SETTING_KEY.SHOW_DISPLAYNAME,
          false);

  public int Id;
  public String Title;
  public String Displayname;
  public String Album;
  public int AlbumId;
  public String Artist;
  public int ArtistId;
  public long Duration;
  public String RealTime;
  public String Url;
  public long Size;
  public String Year = "";
  public String TitleKey;
  public long AddTime;
  private boolean isLove;

  public Song() {
  }

  public Song(int id, String displayname, String title, String album,
      int albumid, String artist, int artistId, long duration, String realTime,
      String url, long size, String year, String titleKey, long addTime) {
    Id = id;
    Title = title;
    Displayname = displayname;
    Album = album;
    AlbumId = albumid;
    Artist = artist;
    ArtistId = artistId;
    Duration = duration;
    RealTime = realTime;
    Url = url;
    Size = size;
    Year = year;
    TitleKey = titleKey;
    AddTime = addTime;
  }

  public Song(int id) {
    Id = id;
  }

  public Song(Song info) {
    if (info == null) {
      return;
    }
    this.Id = info.getId();
    this.Title = info.getTitle();
    this.Displayname = info.getDisplayname();
    this.Album = info.getAlbum();
    this.AlbumId = info.getAlbumId();
    this.Artist = info.getArtist();
    this.Duration = info.getDuration();
    this.RealTime = info.getRealTime();
    this.Url = info.getUrl();
    this.Size = info.getSize();
    this.Year = info.getYear();
    this.TitleKey = info.getTitleKey();
    this.AddTime = info.getAddTime();
  }

  @Override
  public Object clone() {
    Object o = null;
    try {
      o = super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。
    } catch (CloneNotSupportedException e) {
      System.out.println(e.toString());
    }
    return o;
  }

  @Override
  public String toString() {
    return "Song{" +
        "Id=" + Id +
        ", Title='" + Title + '\'' +
        ", Displayname='" + Displayname + '\'' +
        ", Album='" + Album + '\'' +
        ", AlbumId=" + AlbumId +
        ", Artist='" + Artist + '\'' +
        ", Duration=" + Duration +
        ", RealTime='" + RealTime + '\'' +
        ", Url='" + Url + '\'' +
        ", Size=" + Size +
        ", Year=" + Year +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Song && ((Song) o).getId() == this.getId();
  }

  public long getAddTime() {
    return AddTime;
  }

  public void setAddTime(long addTime) {
    AddTime = addTime;
  }

  public String getTitleKey() {
    return TitleKey;
  }

  public void setTitleKey(String titleKey) {
    TitleKey = titleKey;
  }

  public void setYear(String year) {
    Year = year;
  }

  public String getYear() {
    return Year;
  }

  public void setTitle(String title) {
    Title = title;
  }

  public String getTitle() {
    return Title;
  }

  public int getAlbumId() {
    return AlbumId;
  }

  public void setAlbumId(int albumId) {
    AlbumId = albumId;
  }

  public int getArtistId() {
    return ArtistId;
  }

  public void setArtistId(int artistId) {
    ArtistId = artistId;
  }

  public String getRealTime() {
    return RealTime;
  }

  public void setRealTime(String realTime) {
    RealTime = realTime;
  }

  public int getId() {
    return Id;
  }

  public void setId(int id) {
    Id = id;
  }

  public String getDisplayname() {
    return Displayname;
  }

  public void setDisplayname(String displayname) {
    Displayname = displayname;
  }

  public String getAlbum() {
    return Album;
  }

  public void setAlbum(String album) {
    Album = album;
  }

  public long getDuration() {
    if (Duration <= 0) {
      IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
      try {
        File file = new File(Url);
        if(!file.exists()){
          return Duration;
        }
        ijkMediaPlayer.setDataSource(Url);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        ijkMediaPlayer.prepareAsync();
        Thread.sleep(20);
        Duration = ijkMediaPlayer.getDuration();
        Timber.v("Duration: %s", Duration);
        if (Duration > 0) {
          ContentValues contentValues = new ContentValues();
          contentValues.put(MediaStore.Audio.Media.DURATION, Duration);
          int updateCount = App.getContext().getContentResolver()
              .update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                  contentValues, MediaStore.Audio.Media._ID + "=?", new String[]{Id + ""});
          Timber.v("UpdateCount: %s", updateCount);
        }
      } catch (Exception e) {
        Timber.v(e);
      }
    }
    return Duration;
  }

  public void setDuration(long duration) {
    Duration = duration;
  }

  public String getUrl() {
    return Url;
  }

  public void setUrl(String url) {
    Url = url;
  }

  public String getArtist() {
    return Artist;
  }

  public void setArtist(String artist) {
    Artist = artist;
  }

  public long getSize() {
    return Size;
  }

  public void setSize(long size) {
    Size = size;
  }

  public String getShowName() {
    return !SHOW_DISPLAYNAME ? Title : Displayname;
  }

  public boolean isLove() {
    return isLove;
  }

  public void setIsLove(boolean love) {
    isLove = love;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.Id);
    dest.writeString(this.Title);
    dest.writeString(this.Displayname);
    dest.writeString(this.Album);
    dest.writeInt(this.AlbumId);
    dest.writeString(this.Artist);
    dest.writeInt(this.ArtistId);
    dest.writeLong(this.Duration);
    dest.writeString(this.RealTime);
    dest.writeString(this.Url);
    dest.writeLong(this.Size);
    dest.writeString(this.Year);
    dest.writeString(this.TitleKey);
    dest.writeLong(this.AddTime);
  }

  protected Song(Parcel in) {
    this.Id = in.readInt();
    this.Title = in.readString();
    this.Displayname = in.readString();
    this.Album = in.readString();
    this.AlbumId = in.readInt();
    this.Artist = in.readString();
    this.ArtistId = in.readInt();
    this.Duration = in.readLong();
    this.RealTime = in.readString();
    this.Url = in.readString();
    this.Size = in.readLong();
    this.Year = in.readString();
    this.TitleKey = in.readString();
    this.AddTime = in.readLong();
  }

  public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
    @Override
    public Song createFromParcel(Parcel source) {
      return new Song(source);
    }

    @Override
    public Song[] newArray(int size) {
      return new Song[size];
    }
  };
}
