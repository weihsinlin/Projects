## Census Income Project


### Introduction

  Income level has become a topic people pay a lot of attention to. In this project, I will analyze people’s income level, below or above 50,000, from census income data set denoted by Ronny Kohavi and Barry Becker to the UCI Machine Learning Repository. The features I will use to for the analysis are age, work class, final weight, marital status, occupation, relationship in the family, race, sex, capital gain, capital loss, working hours per week, and native country. The goal of this project is to use the feature to build a reasonable model in order to be able to determine if a new observation with all the features stated above has income less than or more than 50,000. To accomplish the goal, I am going to utilize classification tree model, bagging tree, and random forest, and, in the end, I will pick a model that fits the data the best to consider as the final model.  

### Data 

The data set for this project is the Census Income Data Set (https://archive.ics.uci.edu/ml/datasets/Census+Income) donated by Ronny Kohavi and Barry Becker to the UCI Machine Learning Repository.

The data set describes 15 variables on a sample of individuals from the US Census database. The prediction task is to determine whether a person makes over 50K a year.

### Prerequisites

This project uses language R. 

The packages used: 

  - ggplot2
  
  - rpart
  
  - ROCR
  
  - randomForest
  
  - caret
  
  
### Structures
 
#### Data Cleaning and Exploratory Data Analysis

 - Handling missing values
 
 - Handling outliers
 
 - Changing scales
 
 - Summary statistics
 
 - Visualizing distributions
 
 - Association between each predictor and the response
 
#### Model Building
 
##### - Build a Classification Tree

 - Fit a classification tree
 
 - Make plots and choose optimal tuning parameters
 
 - Report your 5 important features, with their variable importance statistics

 - Report the training accuracy rate

 - Plot the ROC curve, and report its area under the curve (AUC) statistic
 
##### - Build a Bagged Tree

 - Train a bagging tree classifier
 
 - Make plots and choose optimal tuning parameters
 
 - Report your 5 important features, with their variable importance statistics

 - Report the training accuracy rate

 - Plot the ROC curve, and report its area under the curve (AUC) statistic

##### - Build a Random Forest

 - Train a random forest classifier
 
 - Make plots and choose optimal tuning parameters
 
 - Report your 5 important features, with their variable importance statistics

 - Report the training accuracy rate

 - Plot the ROC curve, and report its area under the curve (AUC) statistic
 
 
#### Model Selection

 - Validate your best supervised classifier on the test set
 
 - Compute the confusion matrix
 
 - Using the class "over 50K a year" as the positive event, calculate the Sensitivity or True Positive Rate (TPR), and the Specificity or True Negative Rate (TNR)
 
 - Plot the ROC curves of all the classifiers
 
### Conclusion

By comparing confusion matrix, specificity, sensitivity, testing accuracy, ROC curve, and AUC, the classifier gets selected is classification tree with complexity parameter of 0.0022. 