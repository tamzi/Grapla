# Git Hook Enforcement Strategy

## The Problem: Hooks Can Be Bypassed

Git hooks are **client-side** validation tools that run on a developer's local machine. By design,
they can be bypassed using the `--no-verify` flag. This is intentional in Git's design philosophy -
developers have full control over their local repository. While this flexibility is useful in
emergencies, it means hooks alone cannot enforce rules.

## Why Developers Bypass Hooks

Understanding motivations helps us create better solutions:

1. **Legitimate reasons:**
    - Emergency hotfixes under time pressure
    - Committing work-in-progress before leaving
    - Dealing with flaky tests
    - Working around hook bugs

2. **Problematic reasons:**
    - Avoiding fixing legitimate issues
    - Impatience with slow checks
    - Not understanding why rules exist
    - Lack of accountability

## Multi-Layer Defense Strategy

Since hooks can be bypassed, we use multiple layers of defense:

### Layer 1: Client-Side Hooks (First Line of Defense)

**Location:** `.git/hooks/` in each developer's local repository

**Hooks installed:**

- `pre-commit` - Validates staged changes before commit
- `pre-push` - Validates commit history and runs tests before push
- `commit-msg` - Validates commit message format

**Advantages:**

- ✅ Immediate feedback (seconds)
- ✅ Catches issues before they enter git history
- ✅ Doesn't waste CI resources
- ✅ Works offline

**Limitations:**

- ❌ Can be bypassed with `--no-verify`
- ❌ Not installed automatically with `git clone`
- ❌ Can be disabled by changing `core.hooksPath`
- ❌ Developers can delete `.git/hooks/` files

**Installation:** Run the install-git-hooks.sh script in the scripts directory.

**Verification:** Run the verify-hooks.sh script in the scripts directory.

### Layer 2: Server-Side Hooks (Cannot Be Bypassed)

**Location:** Git server (GitHub, GitLab, Bitbucket, self-hosted)

**Hook:** `pre-receive` - Runs on server before accepting push

**Advantages:**

- ✅ **Cannot be bypassed** - runs on server, not client
- ✅ Applies to all developers uniformly
- ✅ Enforces rules even if client hooks are disabled
- ✅ Final gatekeeper before code enters main repository

**Limitations:**

- ❌ Requires server access to install
- ❌ Feedback is slower (after push attempt)
- ❌ May require admin privileges
- ❌ Not all Git hosting services support custom hooks

**Installation (requires server access):**

For self-hosted Git servers, copy the pre-receive.sh script from the scripts directory to the
server's hooks directory and make it executable.

For GitHub: Use GitHub Actions (see Layer 3)
For GitLab: Use GitLab CI (see Layer 3)

### Layer 3: CI/CD Checks (Automated Validation)

**Location:** GitHub Actions, GitLab CI, Jenkins, etc.

**When:** After push, before merge

**Advantages:**

- ✅ Works on any Git hosting platform
- ✅ Visible to all team members
- ✅ Can block PR/MR merges
- ✅ Provides detailed reports
- ✅ Can run additional expensive checks

**Limitations:**

- ❌ Slower feedback (minutes vs seconds)
- ❌ Wastes CI resources on preventable failures
- ❌ Code already in remote repository by the time it fails

**Our CI checks:**

- Build validation
- Unit tests
- Code quality (detekt, lint)
- Commit history validation
- Documentation checks

**Configuration:** See `.github/workflows/` for details

### Layer 4: Build-Time Warnings

**Location:** Gradle build script

**When:** During any Gradle build

**What it does:** Registers a Gradle task that runs the check-hooks-on-build.sh script to remind
developers to install hooks.

**Advantages:**

- ✅ Reminds developers to install hooks
- ✅ Runs automatically during development
- ✅ Non-intrusive (warning only, doesn't fail build)

**Limitations:**

- ❌ Just a reminder, cannot enforce
- ❌ Only helps if developers notice the warning

### Layer 5: Code Review (Human Oversight)

**Location:** Pull Request / Merge Request review process

**What reviewers should check:**

- Commit history follows rules
- Code quality meets standards
- Tests are included and passing
- Documentation is updated

**Advantages:**

- ✅ Human judgment for edge cases
- ✅ Educational opportunity
- ✅ Catches issues automation might miss

**Limitations:**

- ❌ Time-consuming
- ❌ Depends on reviewer diligence
- ❌ Can be rushed under pressure

### Layer 6: Team Culture (Foundation)

**Location:** Team practices and communication

**Key elements:**

- Document **why** rules exist, not just **what** they are
- Make following rules easier than bypassing them
- Celebrate good commit hygiene
- Learn from violations rather than blame
- Improve rules based on feedback

**Advantages:**

- ✅ Sustainable long-term
- ✅ Addresses root causes
- ✅ Improves team collaboration
- ✅ Reduces enforcement burden

**Limitations:**

- ❌ Takes time to build
- ❌ Requires ongoing effort
- ❌ Can be disrupted by turnover

## Recommended Configuration

### For Individual Developers

**Minimum (required):**

1. Install client-side hooks: `./scripts/install-git-hooks.sh`
2. Verify installation: `./scripts/verify-hooks.sh`

**Recommended:**

1. Understand why rules exist (read docs)
2. Only use `--no-verify` in genuine emergencies
3. Fix violations before pushing, not after
4. Help improve rules that cause friction

### For Project Maintainers

**Phase 1: Local Enforcement (easiest)**

- ✅ Provide hook installation scripts
- ✅ Add build-time warnings
- ✅ Document rules clearly
- ✅ Make setup instructions prominent

**Phase 2: Automated Enforcement (recommended)**

- ✅ Configure CI/CD checks
- ✅ Block PR merges on failures
- ✅ Send notifications on violations
- ✅ Track metrics

**Phase 3: Server-Side Enforcement (strongest)**

- ⬜ Install server-side hooks (if possible)
- ⬜ Configure GitHub branch protection rules
- ⬜ Require status checks before merge
- ⬜ Restrict who can bypass checks

### For Organizations

**Technical measures:**

1. Server-side hooks on all repositories
2. Branch protection rules enforced
3. CI/CD pipelines mandatory
4. Regular audits of violations

**Cultural measures:**

1. Training on git best practices
2. Clear documentation of standards
3. Easy-to-use automation tools
4. Recognition for good practices

## Handling Emergencies

Sometimes bypassing hooks is genuinely necessary:

**Legitimate emergency scenarios:**

- Critical production bug requiring immediate hotfix
- Reverting a broken commit that blocks everyone
- Working around a hook bug that prevents valid commits

**How to handle responsibly:**

1. **Document the bypass:** Include in your commit message why --no-verify was used and create a
   TODO to fix validation issues in a follow-up commit.

2. **Fix properly afterward:** Use git rebase to clean up the commit history, split commits, and
   fix violations.

3. **Learn and improve:**
    - Was the bypass necessary?
    - Could better tools prevent future bypasses?
    - Should the rule be adjusted?

## Monitoring and Metrics

Track these metrics to measure enforcement effectiveness:

- **Hook installation rate:** % of developers with hooks installed
- **Bypass frequency:** How often `--no-verify` is used
- **CI failure rate:** % of pushes that fail CI checks
- **Time to fix:** How long violations take to fix
- **Rule violations by type:** Which rules are broken most often

**How to track:** Check git logs for --no-verify mentions, monitor CI failure rates through your
CI system, and verify hook installation status using the verify-hooks.sh script.

## Continuous Improvement

The enforcement strategy should evolve:

1. **Regular reviews:** Quarterly assessment of rule effectiveness
2. **Gather feedback:** Survey developers on pain points
3. **Update tools:** Improve hooks to be faster and more accurate
4. **Adjust rules:** Remove rules that cause unnecessary friction
5. **Automate more:** Make compliance easier than non-compliance

## FAQ

### Q: Why can't we just prevent `--no-verify`?

**A:** Git is designed to give developers full control over their local repository. We can't
prevent `--no-verify` without modifying Git itself. Instead, we use multiple layers of defense.

### Q: Should we punish developers who bypass hooks?

**A:** Generally no. Punishment creates adversarial relationships. Instead:

- Understand why they bypassed
- Make following rules easier
- Use server-side hooks to prevent issues
- Focus on education and tools

### Q: Our hooks are too slow, what should we do?

**A:** Slow hooks encourage bypassing. Profile and optimize scripts, move expensive checks to CI,
use incremental checks, consider parallel execution, and cache results when possible.

### Q: How do we handle third-party contributors?

**A:** External contributors may not have hooks installed. Server-side hooks and comprehensive CI
checks are critical. Documentation should be clear and welcoming.

### Q: Can hooks interfere with rebasing/cherry-picking?

**A:** Yes, hooks can make interactive rebasing tricky. Temporarily disable hooks during complex
operations using git config, then re-enable afterward.

## Summary

**Key Takeaway:** Client-side hooks are valuable but bypassable. Use multiple layers:

1. ✅ **Install client hooks** - Fast feedback for most developers
2. ✅ **Configure CI checks** - Automated enforcement for all pushes
3. ⭐ **Add server hooks** - Ultimate enforcement (if possible)
4. ✅ **Build team culture** - Make following rules the path of least resistance

**For most projects:**

- Client hooks (required)
- CI checks (required)
- Code review (required)
- Server hooks (nice to have)

**Remember:** The goal is maintainable code quality, not perfect enforcement. Focus on making
it easy to do the right thing rather than making it impossible to do the wrong thing.

---

**Related Documentation:**

- `scripts/README.md` - Hook installation and usage
- `docs/agents/commitRules.md` - Commit standards (if exists)
- `.github/workflows/` - CI/CD configuration
