package seedu.planner.logic.events.schedule;

import java.time.LocalTime;
import java.util.List;

import seedu.planner.commons.core.Messages;
import seedu.planner.commons.core.index.Index;
import seedu.planner.logic.commands.UndoableCommand;
import seedu.planner.logic.commands.schedulecommand.ScheduleCommand;
import seedu.planner.logic.commands.schedulecommand.UnscheduleCommand;
import seedu.planner.logic.events.Event;
import seedu.planner.logic.events.exceptions.EventException;
import seedu.planner.model.Model;
import seedu.planner.model.activity.Activity;
import seedu.planner.model.day.ActivityWithTime;
import seedu.planner.model.day.Day;

/**
 * An event representing a 'unschedule' command.
 */
public class UnscheduleEvent implements Event {
    private final Index activityIndex;
    private final LocalTime startTime;
    private final Index dayIndex;
    private final Index activityUnscheduledIndex;

    public UnscheduleEvent(Index activityIndex, Index dayIndex, Model model) throws EventException {
        this.activityIndex = activityIndex;
        this.dayIndex = dayIndex;
        this.startTime = generateActivityStartTime(model);
        this.activityUnscheduledIndex = generateActivityUnscheduledIndex(model);
    }

    public UndoableCommand undo() {
        return new ScheduleCommand(activityUnscheduledIndex, startTime, dayIndex);
    }

    public UndoableCommand redo() {
        return new UnscheduleCommand(activityIndex, dayIndex);
    }

    /**
     * A method to obtain the start time of the activity to be unscheduled from a particular day.
     * @param model Current model of the application
     * @return the start time of the activity to be unscheduled.
     * @throws EventException
     */
    private LocalTime generateActivityStartTime(Model model) throws EventException {
        List<Day> lastShownDays = model.getFilteredItinerary();
        Day dayToEdit = lastShownDays.get(dayIndex.getZeroBased());
        List<ActivityWithTime> activitiesInDay = dayToEdit.getListOfActivityWithTime();

        if (activityIndex.getZeroBased() >= activitiesInDay.size()) {
            throw new EventException(Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);
        }
        ActivityWithTime activityToUnschedule = activitiesInDay.get(activityIndex.getZeroBased());
        return activityToUnschedule.getStartDateTime().toLocalTime();
    }

    private Index generateActivityUnscheduledIndex(Model model) throws EventException {
        List<Day> lastShownDays = model.getFilteredItinerary();
        Day dayToEdit = lastShownDays.get(dayIndex.getZeroBased());
        List<ActivityWithTime> activitiesInDay = dayToEdit.getListOfActivityWithTime();

        if (activityIndex.getZeroBased() >= activitiesInDay.size()) {
            throw new EventException(Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);
        }

        Activity activityUnscheduled = dayToEdit.getActivityWithTime(activityIndex).getActivity();

        List<Activity> lastShownActivities = model.getFilteredActivityList();
        return Index.fromZeroBased(lastShownActivities.indexOf(activityUnscheduled));
    }

}
