package seedu.planner.logic.events.schedule;

import java.util.List;

import seedu.planner.commons.core.index.Index;
import seedu.planner.logic.commands.AutoScheduleCommand;
import seedu.planner.logic.commands.UndoableCommand;
import seedu.planner.logic.commands.system.SetItineraryCommand;
import seedu.planner.logic.events.Event;
import seedu.planner.model.Itinerary;
import seedu.planner.model.Model;
import seedu.planner.model.ReadOnlyItinerary;
import seedu.planner.model.field.Address;
import seedu.planner.model.field.NameAndTagWithTime;

/**
 * An event representing an 'autoschedule' command.
 */
public class AutoScheduleEvent implements Event {
    private final ReadOnlyItinerary previousItinerary;
    private final List<NameAndTagWithTime> draftSchedule;
    private final Address address;
    private final List<Index> days;

    public AutoScheduleEvent(List<NameAndTagWithTime> draftSchedule, Address address, List<Index> days, Model model) {
        this.draftSchedule = draftSchedule;
        this.address = address;
        this.days = days;
        this.previousItinerary = new Itinerary(model.getItinerary());
    }

    public UndoableCommand undo() {
        return new SetItineraryCommand(previousItinerary);
    }

    public UndoableCommand redo() {
        return new AutoScheduleCommand(draftSchedule, address, days);
    }
}
