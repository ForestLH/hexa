// #![doc = include_str!("../README.md")]
// #![deny(missing_docs, rustdoc::broken_intra_doc_links)]
mod error;
mod macros;
pub mod timer;

pub use error::{HexaError, Result};
