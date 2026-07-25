# domain/src/main/kotlin/co/anitrend/domain/staff/

## Responsibility

Defines staff listing, staff entity contracts, language enum, and sort enum.

## Design Patterns

`StaffUseCase` wraps `IStaffRepository.Paged`. `Staff` implements `IStaff` contracts for identity, name, image, language, and favourite state.

## Data & Control Flow

Paged staff params flow into the use case and repository, which returns staff results for display.

## Integration Points

Used by staff search, media staff, and detail surfaces.
