import React from 'react';
import { View, StyleSheet } from 'react-native';
import CustomButton from './js/components/CustomButton';

export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <CustomButton
          text="Strom"
          onPress={() => {
            alert("Hi there!!!");
          }}
          backgroundColor="#FFF500"
          energyType='Strom'
        />
        <CustomButton
          text="Gas"
          onPress={() => {
            alert("Hi there!!!");
          }}
          backgroundColor="#41AFFF"
          energyType='Gas'
        />

      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#ffffff',
    flex: 1,
  }
});