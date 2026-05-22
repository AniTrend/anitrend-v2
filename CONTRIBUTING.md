# Contributing

When contributing to this repository, please first discuss the change you wish to make via github issue, email, discord or any other method with the owners of this repository before making a change.

Please note we have a code of conduct, please follow it in all your interactions with the project.

## Contributing Guidelines

Please ensure your **issues** adheres to the following guidelines:

- Search previous suggestions for duplicates before making a new one.
- Individual issues for each suggestion, bug or feature.
- Titles should use a scoped prefix so ownership is visible at a glance, for example `[data] Harden join-table upserts` or `[android:navigation] Align Compose drawer animation`
- Prefer existing scope families already used in the repository, such as `[data]`, `[feature-media]`, `[testing]`, `[build]`, or `[android:navigation]`
- Use labels for taxonomy such as feature, bug fix, refactor, or docs rather than encoding taxonomy into the issue title itself

Please ensure your **pull request** adheres to the following guidelines:

- Make an individual pull requests for each issue, and make sure the issue is linked to the PR
- Titles should follow the conventional format `<type>(<scope>): <small summary>` and stay aligned with the branch intent
- Be sure not to stage any files excluded in any of the `.gitignore` files
- Assure that your commits mention any relevant **issues** or other **pull requests**
- Automated pull requests should follow the same branch naming rules as contributor pull requests


## Quality Standards

For any pull requests created exhaustive unit tests are mandatory, showcasing the test cases you've guarded against and the extent of your use case coverage. If you have any questions regarding this please feel free to ask. In addition to these standards please follow the following

- **Branch Naming Convention**: Create branches with the prefix matching the change type, following the format `<type>/<issue>-<short-description>` when an issue already exists. If there is no issue number yet, use `<type>/<short-description>` temporarily and link the branch back to the issue as soon as it exists.
- Supported branch prefixes should stay aligned with `.github/release-drafter-config.yml`, which drives branch-based auto-labeling and release categorization
- Current supported primary prefixes are:
  - `feat` - A new feature
  - `fix` - A bug fix
  - `chore` - Routine tasks, dependencies, and maintenance
  - `docs` - Documentation only changes
  - `refactor` - Code change that neither fixes a bug nor adds a feature
  - `test` - Adding missing tests or correcting existing tests
  - `build` - Changes that affect the build system or dependencies
  - `ci` - Changes to CI configuration files or automation
  - `revert` - Reverting a previous commit
- Branch examples:
  - `feat/1208-anime-themes-source-migration`
  - `fix/1177-room-ksp-migration-failure`
  - `docs/1234-update-contributing-guidelines`
  - `ci/update-graphql-schemas`
- **Commit Message Convention**: Use the format `<type>(<scope>): <brief summary>` when a clear scope exists. Scope should be a module, package, feature area, or build surface.
- Commit examples:
  - `chore(buildSrc): align plugin dependency versions`
  - `fix(data): guard empty cache writes`
  - `docs(contributing): clarify branch and PR naming`
  - `ci(graphql): refresh GraphQL schemas`
- **Pull Request Title Convention**: Use the format `<type>(<scope>): <small summary>` and keep the title consistent with the branch intent and changed area.
- Pull request title examples:
  - `feat(media): add anime themes enrichment`
  - `refactor(data): standardize module-owned embed mappers`
  - `ci(graphql): refresh GraphQL schemas`
- Assign yourself to an issue prior to picking up any work to ensure that multiple people don't start working on the same thing
- Use [discussions](https://github.com/AniTrend/anitrend-v2/discussions) for general development related queries or planning information to keep our issues clutter free
- CI-generated maintenance updates should use the same conventions as contributor work. Example branch: `ci/update-graphql-schemas`, example commit: `ci(graphql): refresh GraphQL schemas`

### Setting up Git Hooks

To enforce the branch naming convention locally, run the setup script after cloning:

```bash
./.githooks/setup.sh
```

This will configure Git to use the repository's custom hooks that validate branch names before commits.

Please see [Git Best Practises](https://deepsource.io/blog/git-best-practices/)

## Code of Conduct

### Our Pledge

In the interest of fostering an open and welcoming environment, we as contributors and maintainers pledge to making participation in our project and our community a harassment-free experience for everyone, regardless of age, body size, disability, ethnicity, gender identity and expression, level of experience, nationality, personal appearance, race, religion, or sexual identity and orientation.

### Our Standards

Examples of behavior that contributes to creating a positive environment include:

* Using welcoming and inclusive language
* Being respectful of differing viewpoints and experiences
* Gracefully accepting constructive criticism
* Focusing on what is best for the community
* Showing empathy towards other community members

Examples of unacceptable behavior by participants include:

* The use of sexualized language or imagery and unwelcome sexual attention or advances
* Trolling, insulting/derogatory comments, and personal or political attacks
* Public or private harassment
* Publishing others' private information, such as a physical or electronic address, without explicit permission
* Other conduct which could reasonably be considered inappropriate in a professional setting

### Our Responsibilities

Project maintainers are responsible for clarifying the standards of acceptable behavior and are expected to take appropriate and fair corrective action in response to any instances of unacceptable behavior.

Project maintainers have the right and responsibility to remove, edit, or reject comments, commits, code, wiki edits, issues, and other contributions that are not aligned to this Code of Conduct, or to ban temporarily or permanently any contributor for other behaviors that they deem inappropriate, threatening, offensive, or harmful.

### Scope

This Code of Conduct applies both within project spaces and in public spaces when an individual is representing the project or its community. Examples of representing a project or community include using an official project e-mail address, posting via an official social media account, or acting as an appointed representative at an online or offline event. Representation of a project may be further defined and clarified by project maintainers.

### Enforcement

Instances of abusive, harassing, or otherwise unacceptable behavior may be reported by contacting the project team at anitrendapp@gmail.com. The project team will review and investigate all complaints, and will respond in a way that it deems appropriate to the circumstances. The project team is obligated to maintain confidentiality with regard to the reporter of an incident. Further details of specific enforcement policies may be posted separately.

Project maintainers who do not follow or enforce the Code of Conduct in good faith may face temporary or permanent repercussions as determined by other members of the project's leadership.

### Attribution

This Code of Conduct is adapted from the [Contributor Covenant][homepage], version 1.4, available at [http://contributor-covenant.org/version/1/4][version]

[homepage]: http://contributor-covenant.org
[version]: http://contributor-covenant.org/version/1/4/
 
 
Thank you for your contribution!
