package server;

/**
 * A class that stores parcel info.
 */
public class Parcel {

  private final Integer id;
  private Integer recipient;
  private Integer sender;
  private String downloaded;
  private String parcel;

  /**
   * Constructor for a parcel object.
   * @param id - The unique ID for this parcel.
   * @param recipient - The trueKey of the recipient.
   * @param sender - The trueKey of the sender.
   * @param downloaded - True if the parcel has been downloaded into the game, else false.
   * @param parcel - The JSON data for the item(s) sent.
   */
  public Parcel(Integer id, Integer recipient, Integer sender, String downloaded, String parcel) {
    this.id = id;
    this.recipient = recipient;
    this.sender = sender;
    this.downloaded = downloaded;
    this.parcel = parcel;
  }

  public Integer getId() {
    return this.id;
  }

  public Integer getRecipient() {
    return this.recipient;
  }

  public Integer getSender() {
    return this.sender;
  }

  public boolean getDownloaded() {
    if (downloaded.equals("true")) {
      return true;
    } else if (downloaded.equals("false")) {
      return false;
    } else {
      throw new RuntimeException("ERROR: There was an issue with the" +
          "formatting of the downloaded attribute.");
    }
  }

  public String getParcel() {
    return this.parcel;
  }

}
