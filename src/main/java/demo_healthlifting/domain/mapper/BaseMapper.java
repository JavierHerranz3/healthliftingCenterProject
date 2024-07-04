package demo_healthlifting.domain.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public interface BaseMapper<I, O> {

	O fromInputToOutput(I input);

	I fromOutputToInput(O output);

	List<O> fromInputToOutput(List<I> inputList);

	List<I> fromOutputToInput(List<O> outputList);

	default Optional<O> fromInputToOutput(Optional<I> input) {
		return input.isPresent() ? Optional.<O>of(fromInputToOutput(input.get())) : Optional.<O>empty();
	}

	default Optional<I> fromOutputToInput(Optional<O> output) {
		return output.isPresent() ? Optional.<I>of(fromOutputToInput(output.get())) : Optional.<I>empty();
	}

	default Page<O> fromInputToOutput(Page<I> inputPage) {
		return inputPage == null ? null
				: new PageImpl<>(fromInputToOutput(inputPage.getContent()), inputPage.getPageable(),
						inputPage.getTotalElements());
	}

	default Page<I> fromOutputToInput(Page<O> outputPage) {
		return outputPage == null ? null
				: new PageImpl<>(fromOutputToInput(outputPage.getContent()), outputPage.getPageable(),
						outputPage.getTotalElements());
	}
}
