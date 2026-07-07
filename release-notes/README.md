# Release notes

One Markdown file per release, **named exactly after its git tag**:

```
release-notes/
├── v4.4.1.md      -> published when tag  v4.4.1  is pushed
├── v4.4.2.md      -> published when tag  v4.4.2  is pushed
└── ...
```

The `Build native packages` workflow (`.github/workflows/release.yml`) reads
`release-notes/${{ github.ref_name }}.md` and uses it as the GitHub Release body,
then appends the auto-generated commit list below it.

## How to cut a release

1. Copy the previous file to the new tag name and write the highlights:
   `cp release-notes/v4.4.1.md release-notes/v4.4.2.md` and edit it.
2. Commit it on `main`.
3. Tag and push — the file name (without `.md`) must match the tag exactly,
   leading `v` included:
   ```bash
   git tag v4.4.2
   git push origin v4.4.2
   ```

If the file for a tag is missing, the release is still created — it just carries
only the auto-generated commit list (the workflow logs a warning, it does not
fail).
