
package org.missionassetfund.apps.android.interfaces;

public interface OnInputFormListener {

    /**
     * Inform the listener that it should go to the next screen
     * 
     * @param activeFragmentClass
     */
    @SuppressWarnings("rawtypes")
    public void OnNextSelected(Class activeFragmentClass, String value);

    /**
     * Inform the listener that it should go to the previous screen
     * 
     * @param activeFragmentClass
     */
    @SuppressWarnings("rawtypes")
    public void OnBackSelected(Class activeFragmentClass);

    /**
     * Inform the listener that the user has finished the selection
     */
    public void OnFinishSelected();

}
