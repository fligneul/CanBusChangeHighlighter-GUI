# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Explanation of the recommended reverse chronological release ordering.
- JVM argument for mocked data
- Coverage tests for better reliability

### Changed
- Clear message list on new connection
- Update dependencies
- Change Serial read mode from delimiter to message

### Fixed
- Disable connection if COM port or baud rate is not set.

## [1.0] - 2020-02-20
### Added
- First version of CAN Bus message GUI.
- Highlight change when new message is received.
- Add message ID filtering.

[Unreleased]: https://github.com/fligneul/CanBusChangeHighlighter-GUI/compare/v1.0...HEAD
[1.0]: https://github.com/fligneul/CanBusChangeHighlighter-GUI/releases/tag/v1.0