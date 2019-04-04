import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { TouchableOpacity, Text, StyleSheet, Image } from 'react-native';
class CustomButton extends Component {
	render() {
		const { energyType, backgroundColor, text, onPress } = this.props;
		return (
			<TouchableOpacity style={[styles.buttonStyle, { backgroundColor: backgroundColor }]}
				onPress={() => onPress()
				}
			>
				{
					(energyType === 'Gas')
						?
						(<Image source={require('./Gas.png')} />)
						:
						(<Image source={require('./Strom.png')} />)
				}
				<Text style={styles.textStyle}>{text}</Text>
			</TouchableOpacity >
		);
	}
}

CustomButton.propTypes = {
	text: PropTypes.string.isRequired,
	backgroundColor: PropTypes.string.isRequired,
	onPress: PropTypes.func.isRequired,
	energyType: PropTypes.string.isRequired
};

const styles = StyleSheet.create({
	textStyle: {
		fontSize: 20,
		color: '#000000',
		textAlign: 'center',
		fontWeight: 'bold',
		fontSize: 25
	},
	imageStyle: {
		width: 125,
		height: 125
	},
	buttonStyle: {
		flex: 1,
		flexDirection: 'column',
		justifyContent: 'center',
		alignItems: 'center'
	}

});

export default CustomButton;
