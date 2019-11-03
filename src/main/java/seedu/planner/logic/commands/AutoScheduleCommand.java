package seedu.planner.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.planner.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.planner.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.planner.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.planner.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.planner.model.Model.PREDICATE_SHOW_ALL_DAYS;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import seedu.planner.commons.core.Messages;
import seedu.planner.commons.core.index.Index;
import seedu.planner.logic.commands.exceptions.CommandException;
import seedu.planner.logic.commands.result.CommandResult;
import seedu.planner.logic.commands.result.UiFocus;
import seedu.planner.logic.commands.util.HelpExplanation;
import seedu.planner.model.Model;
import seedu.planner.model.activity.Activity;
import seedu.planner.model.activity.ActivityWithCount;
import seedu.planner.model.day.ActivityWithTime;
import seedu.planner.model.day.Day;
import seedu.planner.model.field.Address;
import seedu.planner.model.field.NameOrTagWithTime;

/**
 * Generates a schedule for specified day(s).
 */
public class AutoScheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "autoschedule";
    public static final String MESSAGE_INVALID_SCHEDULE = "Unable to generate a schedule"
            + " with no overlapping activities";
    public static final String MESSAGE_SUCCESS = "Schedule for the day(s) generated!";
    public static final String TIME_FORMAT = "HHmm";
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
    public static final LocalTime DEFAULT_START_TIME = LocalTime.parse("0900", TIME_FORMATTER);
    public static final HelpExplanation MESSAGE_USAGE = new HelpExplanation(
            COMMAND_WORD,
            "Generates a list of activities for specified days based on location, "
                    + " tags and names order given by user.",
            COMMAND_WORD + " (" + PREFIX_TAG + "TAG [START_TIME] || "
                    + PREFIX_NAME + "ACTIVITY_NAME [START_TIME])... "
                    + "[" + PREFIX_ADDRESS + "LOCATION_OF_ACTIVITIES] "
                    + "[" + PREFIX_DAY + "DAY_INDEX...]",
            COMMAND_WORD + " " + PREFIX_TAG + "Dining 1000 " + PREFIX_TAG + "Attraction 1200 "
                    + PREFIX_NAME + "Disneyland 1400 " + PREFIX_TAG + "Dining "
                    + PREFIX_ADDRESS + "Tokyo " + PREFIX_DAY + "1 4 5"
    );
    private List<NameOrTagWithTime> draftSchedule;
    private Optional<Address> address;
    private List<Index> days;

    public AutoScheduleCommand(List<NameOrTagWithTime> draftSchedule, Optional<Address> address, List<Index> days) {
        requireNonNull(draftSchedule);
        this.draftSchedule = draftSchedule;
        this.address = address;
        this.days = days;
    }

    public List<NameOrTagWithTime> getDraftSchedule() {
        return draftSchedule;
    }

    public Optional<Address> getAddress() {
        return address;
    }

    public List<Index> getDays() {
        return days;
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Day> editDays = new ArrayList<>(model.getFilteredItinerary());
        List<Optional<LocalTime>> timeSchedule = fillTimeSchedule(draftSchedule);
        List<Activity> activityListByLocation = new ArrayList<>(filterByLocation(model.getFilteredActivityList()));

        Collections.sort(activityListByLocation);

        if (editDays.size() == 0) {
            throw new CommandException(Messages.MESSAGE_NO_DAYS_AVAILABLE);
        }
        if (days.size() == 0) {
            days = daysToSchedule(editDays.size());
        }
        for (Index dayIndex : days) {
            List<ActivityWithTime> activitiesForTheDay = new ArrayList<>();

            if (dayIndex.getZeroBased() >= editDays.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_DAY_DISPLAYED_INDEX);
            }

            for (int i = 0; i < draftSchedule.size(); i++) {
                boolean isScheduled = false;
                List<Activity> similarActivities = getSimilarActivities(activityListByLocation, draftSchedule.get(i));
                List<ActivityWithCount> activitiesWithCount = updateCount(similarActivities, editDays,
                        activitiesForTheDay, dayIndex);

                Collections.sort(activitiesWithCount);

                for (ActivityWithCount activityWithCount : activitiesWithCount) {
                    int duration = activityWithCount.getActivity().getDuration().value;
                    LocalTime currentTiming = timeSchedule.get(i).get();
                    LocalTime currentActivityEndTime = currentTiming.plusMinutes(duration);

                    if (i == draftSchedule.size() - 1) {
                        if (currentActivityEndTime.isBefore(currentTiming)) {
                            break;
                        }
                        isScheduled = true;
                        activitiesForTheDay.add(activityToSchedule(
                                timeSchedule.get(i).atDate(model.getStartDate().plusDays(dayIndex.getZeroBased())),
                                activityWithCount.getActivity()));
                        break;
                    }

                    OptionalInt nextTimingIndex = getNextTimingIndex(i, timeSchedule);
                    if (nextTimingIndex.isEmpty()) {
                        isScheduled = true;
                        activitiesForTheDay.add(
                                activityToSchedule(
                                    timeSchedule.get(i).atDate(model.getStartDate().plusDays(dayIndex.getZeroBased())),
                                    activityWithCount.getActivity()
                            )
                        );
                        timeSchedule.set(i + 1, currentTiming.plusMinutes(duration));
                        break;
                        //check next timing does not overlap
                    } else {
                        LocalTime startTimeOfNextActivity = timeSchedule.get(nextTimingIndex.getAsInt()).get();

                        if (startTimeOfNextActivity.compareTo(currentActivityEndTime) >= 0) {
                            isScheduled = true;
                            activitiesForTheDay.add(
                                    activityToSchedule(
                                        timeSchedule.get(i)
                                                .atDate(model.getStartDate().plusDays(dayIndex.getZeroBased())),
                                        activityWithCount.getActivity()
                                    )
                            );
                            //the next activity will be given a start time,
                            // if the timing is not the next in line
                            //Eg. 1000 null 1300 -> becomes 1000 1000+30min  1300
                            if (nextTimingIndex.getAsInt() != i + 1) {
                                timeSchedule.set(i + 1, Optional.of(currentActivityEndTime));
                            }
                            break;
                        }
                    }
                }
                if (!isScheduled) {
                    throw new CommandException(MESSAGE_INVALID_SCHEDULE);
                }
            }
            Day editedDay = new Day(activitiesForTheDay);
            editDays.set(dayIndex.getZeroBased(), editedDay);
            model.setDays(editDays);
            model.updateFilteredItinerary(PREDICATE_SHOW_ALL_DAYS);
        }
        return new CommandResult(Messages.MESSAGE_SCHEDULE_ACTIVITY_SUCCESS, new UiFocus[]{UiFocus.AGENDA});
    }

    /**
     * Gets the index of the next timing in the schedule if any.
     */
    private OptionalInt getNextTimingIndex(int i, List<Optional<LocalTime>> timeSchedule) {
        return IntStream.range(i + 1, timeSchedule.size())
                .filter(k -> timeSchedule.get(k).isPresent())
                .findFirst();
    }

    /**
     * Creates an ActivityWithTime to be scheduled.
     */
    private ActivityWithTime toSchedule(LocalDateTime currentDateTime, Activity activity) {
        return new ActivityWithTime(activity, currentDateTime);
    }

    /**
     * Generates a list that track the number of time each activity appears in the timetable.
     *
     * @param activitiesForTheDay list of activities that are generated by auto-scheduling
     */
    private List<ActivityWithCount> updateCount(List<Activity> similarActivities, List<Day> lastShownDays,
                                                List<ActivityWithTime> activitiesForTheDay, Index dayToEdit) {
        List<ActivityWithCount> activityCounts = new ArrayList<>();
        for (Activity similarActivity : similarActivities) {
            long count = 0;
            for (int i = 0; i < lastShownDays.size(); i++) {
                if (i != dayToEdit.getZeroBased()) {
                    count = lastShownDays.get(i).getListOfActivityWithTime()
                            .stream()
                            .filter(activityWithTime -> activityWithTime.getActivity().equals(similarActivity))
                            .count();
                }
            }
            count += (int) activitiesForTheDay
                    .stream()
                    .filter(activityWithTime -> activityWithTime.getActivity().equals(similarActivity))
                    .count();
            activityCounts.add(new ActivityWithCount(similarActivity, (int) count));
        }
        return activityCounts;
    }

    /**
     * Creates a list to track the time of each activity to be carried out.
     */
    private List<Optional<LocalTime>> fillTimeSchedule(List<NameOrTagWithTime> draftSchedule) {
        List<Optional<LocalTime>> timeSchedule = new ArrayList<>();
        for (NameOrTagWithTime nameAndTagWithTime : draftSchedule) {
            timeSchedule.add(nameAndTagWithTime.getTime());
        }
        if (timeSchedule.get(0).isEmpty()) {
            timeSchedule.set(0, Optional.of(DEFAULT_START_TIME));
        }
        return timeSchedule;
    }

    private List<Activity> getSimilarActivities(List<Activity> filteredActivitiesByLocation,
                                                NameOrTagWithTime nameAndTagWithTime) throws CommandException {
        List<Activity> filteredList = filteredActivitiesByLocation
                .stream()
                .filter(activity -> (nameAndTagWithTime.getTag().isPresent()
                        && activity.getTags().contains(nameAndTagWithTime.getTag().get()))
                        ||
                        nameAndTagWithTime.getName().isPresent()
                                && activity.getName().equals(nameAndTagWithTime.getName().get()))
                .collect(Collectors.toList());
        if (filteredList.isEmpty()) {
            if (nameAndTagWithTime.getName().isPresent()) {
                throw new CommandException(String.format(Messages.MESSAGE_ACTIVITY_NAME_NOT_FOUND,
                        nameAndTagWithTime.getName().get()));
            } else {
                throw new CommandException(String.format(Messages.MESSAGE_ACTIVITY_TAG_NOT_FOUND,
                        nameAndTagWithTime.getTag().get()));
            }
        }
        return filteredList;
    }

    /**
     * Returns a list of containing all the days
     */
    private List<Index> daysToSchedule(int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(x -> Index.fromOneBased(x))
                .collect(Collectors.toList());
    }

    /**
     * @return list of activities that has the same location specified.
     */
    private List<Activity> filterByLocation(List<Activity> lastShownActivities) throws CommandException {
        List<Activity> filteredList = lastShownActivities;
        if (address.isPresent()) {
            filteredList = lastShownActivities
                    .stream()
                    .filter(activity -> activity.getAddress().equals(address.get()))
                    .collect(Collectors.toList());
        }
        if (filteredList.size() == 0) {
            throw new CommandException(String.format(Messages.MESSAGE_ADDRESS_NOT_FOUND, address.get()));
        }
        return filteredList;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AutoScheduleCommand
                && draftSchedule.equals(((AutoScheduleCommand) other).draftSchedule))
                && address.equals(((AutoScheduleCommand) other).address)
                && days.equals((((AutoScheduleCommand) other).days));
    }


}
