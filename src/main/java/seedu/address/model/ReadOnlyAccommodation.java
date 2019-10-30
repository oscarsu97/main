package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.accommodation.Accommodation;

/**
 * Unmodifiable view of an Accommodation List
 */
public interface ReadOnlyAccommodation {

    /**
     * Returns an unmodifiable view of the accommodation list.
     * This list will not contain any duplicate accommodations.
     */
    ObservableList<Accommodation> getAccommodationList();

}