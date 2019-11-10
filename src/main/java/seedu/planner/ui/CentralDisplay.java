package seedu.planner.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Skin;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;

import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.scene.control.agenda.Agenda;

import seedu.planner.logic.commands.result.ResultInformation;
import seedu.planner.logic.commands.result.UiFocus;
import seedu.planner.model.accommodation.Accommodation;
import seedu.planner.model.activity.Activity;
import seedu.planner.model.activity.Duration;
import seedu.planner.model.contact.Contact;
import seedu.planner.model.day.ActivityWithTime;
import seedu.planner.model.day.Day;
import seedu.planner.model.field.Name;
import seedu.planner.model.tag.Tag;
import seedu.planner.ui.panels.AccommodationListPanel;
import seedu.planner.ui.panels.ActivityListPanel;
import seedu.planner.ui.panels.ContactListPanel;
import seedu.planner.ui.panels.HelpListPanel;
import seedu.planner.ui.panels.InfoListPanel;

/**
 * A ui for the split window that is displayed at the center of the application.
 */
public class CentralDisplay extends UiPart<Region> {

    private static final String FXML = "CentralDisplay.fxml";
    private static final int CHAR_LIMIT_BEFORE_TRUNCATING_SUMMARY = 220;
    private static final int MAX_MULTIPLE_OF_DAYLIST_SIZE_BEFORE_CLEARING_HASHMAP = 3;
    private SimpleObjectProperty<LocalDate> startDateProperty;
    private SimpleObjectProperty<Name> nameProperty;
    private ObservableList<Day> dayList;
    private HashMap<Integer, Agenda.AppointmentGroup> appointmentGroupHashMap;

    /* To ensure that continuous editing of activity do not make activityToAppointmentGroupHashMap use too much memory*/

    private HashMap<Activity, Agenda.AppointmentGroup> activityToAppointmentGroupHashMap;
    @FXML
    private Accordion sideDisplay;
    @FXML
    private TitledPane activityPane;
    @FXML
    private TitledPane contactPane;
    @FXML
    private TitledPane accommodationPane;
    @FXML
    private TabPane tabDisplay;
    @FXML
    private Tab agendaTab;
    @FXML
    private Tab infoTab;
    @FXML
    private Tab helpTab;

    private final InfoListPanel infoListPanel;
    private final HelpListPanel helpListPanel;
    private final Agenda agenda;

    public CentralDisplay(ObservableList<Day> dayList, ObservableList<Accommodation> accommodationList,
                          ObservableList<Activity> activityList, ObservableList<Contact> contactList,
                          SimpleObjectProperty<LocalDate> startDateProperty,
                          SimpleObjectProperty<Name> nameProperty) {
        super(FXML);
        this.startDateProperty = startDateProperty;
        this.dayList = dayList;
        this.nameProperty = nameProperty;
        this.activityToAppointmentGroupHashMap = new HashMap<>();
        this.infoListPanel = new InfoListPanel();
        this.helpListPanel = new HelpListPanel();

        // initialising agenda
        this.agenda = new Agenda() {
            @Override
            public String getUserAgentStylesheet() {
                return Agenda.class.getResource("/view/" + Agenda.class.getSimpleName() + ".css")
                        .toExternalForm();
            }

            @Override
            public Skin<?> createDefaultSkin() {
                return new AgendaWeekSkin(this) {
                    @Override
                    protected List<LocalDate> determineDisplayedLocalDates() {
                        // the result
                        List<LocalDate> lLocalDates = new ArrayList<>();
                        LocalDate lStartLocalDate = startDateProperty.getValue();
                        if (dayList.size() == 0) {
                            lLocalDates.add(lStartLocalDate);
                        } else {
                            for (int i = 0; i < dayList.size(); i++) {
                                lLocalDates.add(lStartLocalDate.plusDays(i));
                            }
                        }
                        // done
                        return lLocalDates;
                    }
                };
            }
        };
        setupDesign();
        setupContent(dayList, accommodationList, activityList, contactList);
        setupListeners();
    }

    /**
     * Setup the colour, height, width of parts in the central display.
     */
    private void setupDesign() {
        // allows the tabDisplay to expand according to the width of user display
        tabDisplay.prefWidthProperty().bind(this.getRoot().prefWidthProperty());
    }

    /**
     * Setups the content contained within the central display.
     */
    private void setupContent(ObservableList<Day> dayList, ObservableList<Accommodation> accommodationList,
                              ObservableList<Activity> activityList, ObservableList<Contact> contactList) {
        accommodationPane.setContent((new AccommodationListPanel(accommodationList)).getRoot());
        activityPane.setContent((new ActivityListPanel(activityList)).getRoot());
        contactPane.setContent((new ContactListPanel(contactList)).getRoot());
        // expands the activity pane
        sideDisplay.setExpandedPane(activityPane);

        infoTab.setContent(infoListPanel.getRoot());
        helpTab.setContent(helpListPanel.getRoot());

        setupAgendaAppointmentGroups();
        agendaTab.setText(nameProperty.getValue().toString() + " Itinerary");
        agenda.setDisplayedLocalDateTime(startDateProperty.getValue().atStartOfDay());
        updateAgenda(agenda, dayList);
        // disables dragging
        agenda.setAllowDragging(false);
        // disables modify start time and end time by dragging
        agenda.setAllowResize(false);
        // disables right click editing
        agenda.setEditAppointmentCallback((appointment) -> null);
        if (dayList.size() == 0) {
            agendaTab.setContent(null);
        } else {
            agendaTab.setContent(agenda);
        }
    }

    private void setupAgendaAppointmentGroups() {
        if (appointmentGroupHashMap == null) {
            this.appointmentGroupHashMap = new HashMap<>();
            appointmentGroupHashMap.put(1, new Agenda.AppointmentGroupImpl().withStyleClass("brown"));
            appointmentGroupHashMap.put(2, new Agenda.AppointmentGroupImpl().withStyleClass("orange"));
            appointmentGroupHashMap.put(3, new Agenda.AppointmentGroupImpl().withStyleClass("softorange"));
            appointmentGroupHashMap.put(4, new Agenda.AppointmentGroupImpl().withStyleClass("softgreen"));
            appointmentGroupHashMap.put(5, new Agenda.AppointmentGroupImpl().withStyleClass("softcyan"));
            appointmentGroupHashMap.put(6, new Agenda.AppointmentGroupImpl().withStyleClass("softblue"));
            appointmentGroupHashMap.put(7, new Agenda.AppointmentGroupImpl().withStyleClass("yellowgreen"));
            appointmentGroupHashMap.put(8, new Agenda.AppointmentGroupImpl().withStyleClass("grayred"));
            appointmentGroupHashMap.put(9, new Agenda.AppointmentGroupImpl().withStyleClass("gray"));
            appointmentGroupHashMap.put(10, new Agenda.AppointmentGroupImpl().withStyleClass("softpurple"));
        }
    }

    /**
     * Setup listeners that will update the central display.
     */
    private void setupListeners() {
        startDateProperty.addListener((observable, oldValue, newValue) -> {
            updateAgenda(agenda, dayList);
            agenda.setDisplayedLocalDateTime(newValue.atStartOfDay());
        });
        nameProperty.addListener((observable, oldValue, newValue) -> {
            agendaTab.setText(newValue.toString() + " Itinerary");
        });
        agendaTab.setOnSelectionChanged((event) -> {
            if (agendaTab.isSelected()) {
                updateAgenda(agenda, dayList);
                updateSkin(agenda);
            }
        });
        helpTab.setOnSelectionChanged((event) -> {
            if (helpTab.isSelected()) {
                generateCommandHelpSummary();
            }
        });

    }

    /**
     * Refreshes the skin and redetermine the number of dates to display.
     */
    private void updateSkin(Agenda agenda) {
        agenda.setSkin(new AgendaWeekSkin(agenda) {
            @Override
            protected List<LocalDate> determineDisplayedLocalDates() {
                // the result
                List<LocalDate> lLocalDates = new ArrayList<>();

                LocalDate lStartLocalDate = startDateProperty.getValue();
                if (dayList.size() == 0) {
                    lLocalDates.add(lStartLocalDate);
                } else {
                    for (int i = 0; i < dayList.size(); i++) {
                        lLocalDates.add(lStartLocalDate.plusDays(i));
                    }
                }
                // done
                return lLocalDates;
            }
        });
        if (dayList.size() == 0) {
            agendaTab.setContent(null);
        } else {
            agendaTab.setContent(agenda);
        }
    }

    /**
     * Displays the relevant information in infoList from command executed.
     */
    public void changeInfo(ResultInformation[] resultInformation) {
        infoListPanel.changeInfo(resultInformation);
    }

    /**
     * Expands tabs according to the provided {@code uiFocus}.
     */
    public void changeFocus(UiFocus[] uiFocus) {
        for (UiFocus u : uiFocus) {
            switch (u) {
            case AGENDA:
                tabDisplay.getSelectionModel().select(agendaTab);
                updateSkin(agenda);
                updateAgenda(agenda, dayList);
                break;
            case INFO:
                tabDisplay.getSelectionModel().select(infoTab);
                break;
            case HELP:
                tabDisplay.getSelectionModel().select(helpTab);
                break;
            case ACCOMMODATION:
                sideDisplay.setExpandedPane(accommodationPane);
                break;
            case ACTIVITY:
                sideDisplay.setExpandedPane(activityPane);
                break;
            case CONTACT:
                sideDisplay.setExpandedPane(contactPane);
                break;
            default:
                throw new AssertionError(u.toString() + " is not handled in changeFocus.");
            }
        }
    }

    /**
     * Generates a complete summary of all commands available in plan2travel.
     */
    public void generateCommandHelpSummary() {
        helpListPanel.generateCommandHelpSummary();
    }

    /**
     * Updates the agenda with activities in every day of {@code dayList}.
     *
     * @param agenda  the agenda that is updated
     * @param dayList the latest dayList from model
     */
    private void updateAgenda(Agenda agenda, ObservableList<Day> dayList) {
        agenda.appointments().clear();
        for (Day day : dayList) {
            addAppointmentsWithDay(agenda, day, dayList.size());
        }
        if (activityToAppointmentGroupHashMap.size()
                > dayList.size() * MAX_MULTIPLE_OF_DAYLIST_SIZE_BEFORE_CLEARING_HASHMAP) {
            activityToAppointmentGroupHashMap = new HashMap<>();
        }
    }

    /**
     * Adds all activities in a day to the agenda.
     *
     * @param agenda the agenda to add to
     * @param day    the day to search for activities
     */
    private void addAppointmentsWithDay(Agenda agenda, Day day, int numOfDays) {
        Random random = new Random();
        for (ActivityWithTime activityWithTime : day.getListOfActivityWithTime()) {
            Agenda.AppointmentGroup currGroup = getAppointmentGroupOfActivity(activityWithTime.getActivity(), random);
            String textToDisplay = createSummaryOfAppointment(activityWithTime.getActivity(),
                    activityWithTime.getActivity().getDuration(), numOfDays);
            agenda.appointments().add(
                    new Agenda.AppointmentImplLocal()
                            .withStartLocalDateTime(activityWithTime.getStartDateTime())
                            .withEndLocalDateTime(activityWithTime.getEndDateTime())
                            .withSummary(textToDisplay)
                            .withAppointmentGroup(currGroup)
            );
        }
    }

    private Agenda.AppointmentGroup getAppointmentGroupOfActivity(Activity activity, Random random) {
        if (activityToAppointmentGroupHashMap.get(activity) == null) {
            Agenda.AppointmentGroup randomAppointmentGroup = generateRandomGroup(random);
            activityToAppointmentGroupHashMap.put(activity, randomAppointmentGroup);
            return randomAppointmentGroup;
        } else {
            return activityToAppointmentGroupHashMap.get(activity);
        }
    }


    /**
     * Generates a random appointment group from the appointmentGroupHashMap.
     */
    private Agenda.AppointmentGroup generateRandomGroup(Random random) {
        return appointmentGroupHashMap.get(random.nextInt(appointmentGroupHashMap.size()) + 1);
    }

    /**
     * Returns a string with the name and tags of the {@code activity}.
     */
    private String createSummaryOfAppointment(Activity activity, Duration duration, int numOfDays) {
        StringBuilder textToDisplay = new StringBuilder("\n");
        int charLimit = CHAR_LIMIT_BEFORE_TRUNCATING_SUMMARY / numOfDays;
        String nameToDisplay = activity.getName().toString();
        if (nameToDisplay.length() > charLimit) {
            textToDisplay.append(nameToDisplay.substring(0, charLimit + 1));
            textToDisplay.append("...");
        } else {
            textToDisplay.append(nameToDisplay);
        }
        textToDisplay.append('\n');

        StringBuilder tagsToDisplay = new StringBuilder("Tags: ");
        if (duration.value >= 120) {
            boolean isFirst = true;
            for (Tag tag : activity.getTags()) {
                if (isFirst) {
                    isFirst = false;
                    tagsToDisplay.append(tag.toString());
                } else {
                    tagsToDisplay.append(", ")
                            .append(tag.toString());
                }
            }
            if (tagsToDisplay.length() > charLimit) {
                textToDisplay.append(tagsToDisplay.substring(0, charLimit + 1));
                textToDisplay.append("...");
            } else {
                textToDisplay.append(tagsToDisplay);
            }
        }
        return textToDisplay.toString();
    }
}
