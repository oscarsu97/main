package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.logic.commands.AutoScheduleCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.field.Address;

/**
 * Parses input arguments and creates a new AutoScheduleCommand object
 */
public class AutoScheduleCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the AutoScheduleCommand
     * and returns an AutoScheduleScheduleCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AutoScheduleCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG, PREFIX_NAME,
                PREFIX_ADDRESS, PREFIX_DAY);
        if ((!isPrefixPresent(argMultimap, PREFIX_TAG) && !isPrefixPresent(argMultimap, PREFIX_NAME))
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AutoScheduleCommand.MESSAGE_USAGE));
        }

        Address address = null;
        List<Integer> days = new ArrayList<>();
        List<Object> draftSchedule;             //Contains either a Tag class or a Name class

        draftSchedule = getDraftSchedule(argMultimap, PREFIX_TAG, PREFIX_NAME);
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        }
        if (argMultimap.getValue(PREFIX_DAY).isPresent()) {
            days = ParserUtil.parseDaysToSchedule(argMultimap.getValue(PREFIX_DAY).get());
        }

        return new AutoScheduleCommand(draftSchedule, address, days);
    }

    private List<Object> getDraftSchedule(ArgumentMultimap argumentMultimap, Prefix... prefixes) throws ParseException {
        List<PrefixArgument> filteredMultiMap = argumentMultimap.getFilteredArgMultiMap(prefixes);
        List<Object> draftSchedule = new ArrayList<>();
        for (PrefixArgument prefixArgument : filteredMultiMap) {
            if (prefixArgument.getPrefix().equals(PREFIX_TAG)) {
                draftSchedule.add(ParserUtil.parseTag(prefixArgument.getArgValue()));
            }
            if (prefixArgument.getPrefix().equals(PREFIX_NAME)) {
                draftSchedule.add(ParserUtil.parseName(prefixArgument.getArgValue()));
            }
        }
        return draftSchedule;
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean isPrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}