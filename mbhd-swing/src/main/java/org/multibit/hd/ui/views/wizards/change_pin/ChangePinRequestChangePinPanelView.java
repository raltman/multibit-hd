package org.multibit.hd.ui.views.wizards.change_pin;

import com.google.common.base.Optional;
import net.miginfocom.swing.MigLayout;
import org.multibit.hd.ui.events.view.ViewEvents;
import org.multibit.hd.ui.languages.MessageKey;
import org.multibit.hd.ui.views.components.Components;
import org.multibit.hd.ui.views.components.ModelAndView;
import org.multibit.hd.ui.views.components.Panels;
import org.multibit.hd.ui.views.components.panels.PanelDecorator;
import org.multibit.hd.ui.views.components.trezor_display.TrezorDisplayModel;
import org.multibit.hd.ui.views.components.trezor_display.TrezorDisplayView;
import org.multibit.hd.ui.views.fonts.AwesomeIcon;
import org.multibit.hd.ui.views.wizards.AbstractWizard;
import org.multibit.hd.ui.views.wizards.AbstractWizardPanelView;
import org.multibit.hd.ui.views.wizards.WizardButton;

import javax.swing.*;

/**
 * <p>View to provide the following to UI:</p>
 * <ul>
 * <li>Change PIN: Request change PIN</li>
 * </ul>
 *
 * @since 0.0.5
 *  
 */
public class ChangePinRequestChangePinPanelView extends AbstractWizardPanelView<ChangePinWizardModel, ChangePinEnterPinPanelModel> {

  private ModelAndView<TrezorDisplayModel, TrezorDisplayView> trezorDisplayMaV;

  /**
   * @param wizard The wizard managing the states
   */
  public ChangePinRequestChangePinPanelView(AbstractWizard<ChangePinWizardModel> wizard, String panelName) {

    super(wizard, panelName, MessageKey.TREZOR_CONFIRM_CHANGE_PIN_TITLE, AwesomeIcon.LOCK);

  }

  @Override
  public void newPanelModel() {

    // Bind it to the wizard model in case of failure
    getWizardModel().setRequestChangePinPanelView(this);

  }

  @Override
  public void initialiseContent(JPanel contentPanel) {

    contentPanel.setLayout(new MigLayout(
      Panels.migXYLayout(),
      "[]", // Column constraints
      "[]10[]" // Row constraints
    ));

    trezorDisplayMaV = Components.newTrezorDisplayMaV(getPanelName());

    // Need some text here in case device fails just as we being the process
    contentPanel.add(trezorDisplayMaV.getView().newComponentPanel(), "align center,wrap");

    // Ensure we register the components to avoid memory leaks
    registerComponents(trezorDisplayMaV);

  }

  @Override
  protected void initialiseButtons(AbstractWizard<ChangePinWizardModel> wizard) {

    PanelDecorator.addExitCancelNext(this, wizard);

  }

  @Override
  public void fireInitialStateViewEvents() {

    // Initialise with "Unlock" disabled to force users to enter credentials
    ViewEvents.fireWizardButtonEnabledEvent(
      getPanelName(),
      WizardButton.NEXT,
      false
    );

  }

  @Override
  public void afterShow() {

    // Start the change request process immediately
    getWizardModel().requestRemovePin(false);

  }

  @Override
  public void updateFromComponentModels(Optional componentModel) {

    // Do nothing we are a transitional view

  }

  /**
   * @param key The key to the operation text
   */
  public void setOperationText(MessageKey key) {
    this.trezorDisplayMaV.getView().setOperationText(key);
  }

}