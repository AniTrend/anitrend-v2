---
name: graphql-schema-contract-audit
category: api-schema-validation
trigger-intent: Use before generating UI or feature code from a GraphQL operation, when adding or editing .graphql assets, or when auditing paged contracts for alignment across domain, data, mapper, and repository layers.
---

# GraphQL Schema Contract Audit

## Purpose

Block UI implementation until the GraphQL contract is coherent from asset file to domain-facing
`DataState` output.

## Trigger Intent

- A new `.graphql` query or mutation is added.
- A paged result or connection shape changes.
- A remote source annotation, container model, or mapper changed and the contract needs auditing.
- A feature is being scaffolded from an API spec and the data contract must be proven first.

## Repo Anchors

- `data/src/main/assets/graphql/`
- `data/src/main/kotlin/co/anitrend/data/**/datasource/remote/`
- `data/src/main/kotlin/co/anitrend/data/**/model/container/`
- `data/src/main/kotlin/co/anitrend/data/**/Types.kt`
- `domain/src/main/kotlin/co/anitrend/domain/`

## Execution Steps

### 1. Locate the operation and all references

```bash
find data/src/main/assets/graphql -name '*.graphql' | sort
rg -n '^(query|mutation|fragment) ' data/src/main/assets/graphql
rg -n '<OperationName>' data/src/main/assets/graphql data/src/main/kotlin/co/anitrend/data data/src/main/kotlin/co/anitrend/domain
```

### 2. Validate the remote-source annotation

```bash
rg -n '@GraphQuery\\("<OperationName>"\\)' data/src/main/kotlin/co/anitrend/data
```

The operation name in the asset must exactly match the `@GraphQuery("...")` annotation.

### 3. Validate the container model contract

```bash
rg -n '@SerialName\\("<OperationName>"\\)|class .*ContainerModel|interface .*ContainerModel' \
  data/src/main/kotlin/co/anitrend/data
```

The container model must expose the same operation name via `@SerialName("<OperationName>")` and
must map to the same payload shape expected by the controller.

### 4. Validate paged contracts strictly

```bash
rg -n 'PageInfo|pageInfo|hasNextPage|currentPage|lastPage' data/src/main/assets/graphql
rg -n 'PagedList<' data/src/main/kotlin/co/anitrend/data data/src/main/kotlin/co/anitrend/domain
rg -n 'GraphQLController<' data/src/main/kotlin/co/anitrend/data
```

For paged or connection results, confirm all of the following:

- the GraphQL asset includes `pageInfo` or the equivalent pagination metadata
- the container model exposes the paged payload that the mapper expects
- the controller alias resolves to the correct collection shape
- the repository alias exposes `DataState<PagedList<DomainModel>>` or the intended paged type

### 5. Trace the full contract path

Audit this chain in order:

1. `.graphql` asset
2. remote-source `@GraphQuery` method
3. container model `@SerialName`
4. `Types.kt` controller alias
5. mapper and persistence target
6. entity and entity-view converter
7. repository alias
8. domain param and domain model
9. feature or task consumer, only after all previous steps are valid

Use these search commands:

```bash
rg -n 'class .*Mapper|object .*Mapper|interface .*Mapper' data/src/main/kotlin/co/anitrend/data
rg -n 'Converter|EntityView' data/src/main/kotlin/co/anitrend/data
rg -n 'typealias .*Repository|typealias .*Interactor|GraphQLController<' data/src/main/kotlin/co/anitrend/data
rg -n 'Param|entity|repository' data/src/main/kotlin/co/anitrend/domain
```

### 6. Compile before any UI work

```bash
./gradlew :data:compileDebugKotlin --no-daemon --stacktrace
./gradlew :app:assembleDebug --no-daemon --stacktrace
```

## Contract Failure Conditions

Fail the audit if any of the following is true:

- asset operation name and `@GraphQuery` name differ
- container-model `@SerialName` differs from the operation name
- paged assets omit pagination metadata while repository contracts expose paged results
- mapper/controller generics do not match the container-model payload
- the domain param does not cover the required GraphQL variables
- the contract reaches feature code before the data contract compiles cleanly

## Guardrails

- Do not generate UI from the GraphQL file alone.
- Prefer existing module references such as `media`, `medialist`, and `review` before inventing a
  new contract shape.
- Keep feature and task code dependent on interactor aliases, not data repositories or mappers.

## Deliverable

Return:

1. The audited operation.
2. The full contract chain status.
3. Every mismatch found.
4. The compile result.
5. Whether UI work is unblocked or still blocked.
