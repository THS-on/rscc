package ch.imedias.rsccfx;

public class RsccExamplePresenter implements ControlledView {

  ViewController viewParent;

  public void setViewParent(ViewController viewParent){
    this.viewParent = viewParent;
  }
}
