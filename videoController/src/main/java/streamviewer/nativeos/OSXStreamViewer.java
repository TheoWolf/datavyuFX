package streamviewer.nativeos;

import media.DVMedia;
import streamviewer.StreamViewer;
import util.DVStatus;
import util.Identifier;

public class OSXStreamViewer implements StreamViewer {


  @Override
  public Identifier getIdentifier() {
    return null;
  }

  @Override
  public void play() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void stop() {

  }

  @Override
  public void shuttleForward() {

  }

  @Override
  public void shuttleBackward() {

  }

  @Override
  public void jogForward() {

  }

  @Override
  public void jogBackward() {

  }

  @Override
  public void seek(long timeInMillis) {

  }

  @Override
  public void back(long timeInMillis) {

  }

  @Override
  public DVMedia getMedia() {
    return null;
  }

  @Override
  public DVStatus getStatus() {
    return null;
  }

  @Override
  public void setVolume() {

  }

  @Override
  public void getVolume() {

  }

  @Override
  public void visible() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void close() {

  }
}
