# task/review/

## Responsibility

Runs review save, delete, and vote mutations through WorkManager.

## Design Patterns

`ReviewSaveEntryWorker`, `ReviewDeleteEntryWorker`, and `ReviewVoteEntryWorker` each transform router params into `ReviewParam` variants and call the matching data interactor. Provider exposes worker classes through `ReviewTaskRouter`.

## Data & Control Flow

Review UI enqueues save, delete, or vote work. The selected worker calls save, delete, or rate interactor and returns success or failure from terminal load state.

## Integration Points

Consumes `domain/review` params and review data interactors. Used by review editor, delete, and voting flows.
